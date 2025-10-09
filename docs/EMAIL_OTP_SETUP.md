# Email OTP Verification Setup Guide

## ✅ Feature Implemented

Email OTP verification has been added to the registration process. Users must verify their email with a 6-digit code before creating an account.

---

## How It Works

### Registration Flow (3 Steps):

**Step 1: Enter Email**
- User enters their email address
- Clicks "Send Verification Code"

**Step 2: Verify OTP**
- System generates 6-digit OTP
- OTP sent to user's email
- User enters OTP
- OTP valid for 10 minutes
- Clicks "Verify Code"

**Step 3: Complete Registration**
- After successful verification
- User enters name, phone, password
- Creates account

---

## Email Configuration

### Option 1: Gmail (Recommended for Testing)

1. **Enable 2-Factor Authentication**
   - Go to https://myaccount.google.com/security
   - Enable 2-Step Verification

2. **Generate App Password**
   - Visit https://myaccount.google.com/apppasswords
   - Select "Mail" and your device
   - Copy the 16-character password

3. **Update application.properties**
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=xxxx-xxxx-xxxx-xxxx  # App password from step 2
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Option 2: Outlook/Hotmail

```properties
spring.mail.host=smtp-mail.outlook.com
spring.mail.port=587
spring.mail.username=your-email@outlook.com
spring.mail.password=your-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Option 3: Custom SMTP Server

```properties
spring.mail.host=smtp.your-domain.com
spring.mail.port=587
spring.mail.username=noreply@your-domain.com
spring.mail.password=your-smtp-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## Testing Without Email (Development Mode)

If you don't want to configure email for testing, you can:

### Option A: Check Console Logs
The OTP is logged in the application console for development:
```
Generated OTP for patient@example.com: 123456
```

### Option B: Disable OTP Temporarily
Comment out OTP validation in `AuthController`:
```java
// @PostMapping("/verify-otp")
// public ResponseEntity<Map<String, Boolean>> verifyOtp(...) {
//     return ResponseEntity.ok(Map.of("valid", true));  // Always return true
// }
```

### Option C: Use MailHog (Email Testing Tool)
```bash
# Run MailHog locally
docker run -d -p 1025:1025 -p 8025:8025 mailhog/mailhog

# Configure application.properties
spring.mail.host=localhost
spring.mail.port=1025
# No username/password needed

# View emails at: http://localhost:8025
```

---

## Database Table

OTP verification data is stored in:

```sql
CREATE TABLE otp_verifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    otp VARCHAR(6) NOT NULL,
    expiry_time DATETIME NOT NULL,
    verified BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### Automatic Cleanup
- Old unverified OTPs are deleted when new OTP is generated
- Expired OTPs automatically ignored by query

---

## Security Features

✅ **6-digit random OTP** - SecureRandom generator  
✅ **10-minute expiry** - Prevents replay attacks  
✅ **One-time use** - OTP marked as verified after use  
✅ **Email validation** - Must be valid email format  
✅ **Rate limiting ready** - Can add limits per email/IP  

---

## API Endpoints

### Send OTP
```bash
curl -X POST http://localhost:8080/api/auth/send-otp \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com"}'

# Response: { "message": "OTP sent to test@example.com" }
```

### Verify OTP
```bash
curl -X POST http://localhost:8080/api/auth/verify-otp \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","otp":"123456"}'

# Response: { "valid": true } or { "valid": false }
```

---

## Troubleshooting

### Email Not Sending

**Check 1: SMTP Credentials**
- Verify username/password in application.properties
- For Gmail, use App Password, not regular password

**Check 2: Firewall/Port**
```bash
# Test SMTP connection
telnet smtp.gmail.com 587
```

**Check 3: Application Logs**
Look for errors like:
```
AuthenticationFailedException: 535-5.7.8 Username and Password not accepted
```

### OTP Not Received

**Check 1: Spam Folder**
- OTP email might be in spam

**Check 2: Email Format**
- Must be valid email format
- Check for typos

**Check 3: Server Logs**
```bash
# Check for email sending errors
tail -f application.log | grep "Failed to send email"
```

### OTP Expired

- OTPs expire after **10 minutes**
- Click "Change Email" and request new OTP

---

## Code Implementation

### Backend - OTP Service
```java
@Service
public class OtpService {
    
    public void generateAndSendOtp(String email) {
        // Generate 6-digit OTP
        String otp = String.format("%06d", random.nextInt(1000000));
        
        // Save with 10-minute expiry
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);
        OtpVerification verification = new OtpVerification(email, otp, expiryTime);
        otpRepository.save(verification);
        
        // Send email
        emailService.sendOtp(email, otp);
    }
    
    public boolean verifyOtp(String email, String otp) {
        Optional<OtpVerification> opt = otpRepository
            .findByEmailAndOtpAndVerifiedFalseAndExpiryTimeAfter(
                email, otp, LocalDateTime.now());
        
        if (opt.isPresent()) {
            opt.get().setVerified(true);
            otpRepository.save(opt.get());
            return true;
        }
        return false;
    }
}
```

### Frontend - OTP Flow
```javascript
// Step 1: Send OTP
async function sendOtpToEmail() {
  const email = document.getElementById("signupEmail").value;
  await apiPost("/api/auth/send-otp", { email });
  showStep(2);
}

// Step 2: Verify OTP
async function verifyOtpCode() {
  const email = document.getElementById("signupEmail").value;
  const otp = document.getElementById("otpCode").value;
  const res = await apiPost("/api/auth/verify-otp", { email, otp });
  
  if (res.valid) {
    verifiedEmail = email;
    showStep(3);
  } else {
    showAlert("Invalid OTP");
  }
}

// Step 3: Complete registration (only if email verified)
async function submitRegistration() {
  if (!verifiedEmail) {
    showAlert("Email not verified");
    return;
  }
  
  await apiPost("/api/auth/register/patient", {
    email: verifiedEmail,
    fullName: document.getElementById("signupName").value,
    password: document.getElementById("signupPassword").value
  });
}
```

---

## Production Checklist

Before deploying to production:

- [ ] Configure real SMTP server (not Gmail)
- [ ] Use environment variables for email credentials
- [ ] Add rate limiting (max 3 OTPs per email per hour)
- [ ] Add retry logic for email failures
- [ ] Monitor email delivery success rate
- [ ] Add email templates with branding
- [ ] Consider using email service (SendGrid, AWS SES)

---

## Alternative Email Services (Production)

### SendGrid
```properties
spring.mail.host=smtp.sendgrid.net
spring.mail.port=587
spring.mail.username=apikey
spring.mail.password=YOUR_SENDGRID_API_KEY
```

### AWS SES
```properties
spring.mail.host=email-smtp.us-east-1.amazonaws.com
spring.mail.port=587
spring.mail.username=YOUR_SMTP_USERNAME
spring.mail.password=YOUR_SMTP_PASSWORD
```

### Mailgun
```properties
spring.mail.host=smtp.mailgun.org
spring.mail.port=587
spring.mail.username=postmaster@your-domain.mailgun.org
spring.mail.password=YOUR_MAILGUN_PASSWORD
```

---

## Conclusion

Email OTP verification adds an extra layer of security and ensures:
- ✅ Valid email addresses only
- ✅ Prevents spam registrations
- ✅ User owns the email account
- ✅ Professional registration flow

**Status**: ✅ Implemented and tested  
**Build**: ✅ Success  
**Ready**: Configure email credentials to enable

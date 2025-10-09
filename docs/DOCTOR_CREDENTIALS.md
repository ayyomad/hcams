# Doctor Login Credentials

All doctors have the same password: `password123`

## List of 10 Doctors

Based on the DataLoader seed file, here are the 10 doctor accounts:

1. **Dr. Sarah Johnson** (General Medicine)
   - Email: `doctor1@example.com`
   - Password: `password123`

2. **Dr. Michael Brown** (Cardiology)
   - Email: `doctor2@example.com`
   - Password: `password123`

3. **Dr. Emily Davis** (Dermatology)
   - Email: `doctor3@example.com`
   - Password: `password123`

4. **Dr. James Wilson** (Pediatrics)
   - Email: `doctor4@example.com`
   - Password: `password123`

5. **Dr. Jennifer Martinez** (Orthopedics)
   - Email: `doctor5@example.com`
   - Password: `password123`

6. **Dr. Robert Garcia** (Neurology)
   - Email: `doctor6@example.com`
   - Password: `password123`

7. **Dr. Lisa Anderson** (ENT)
   - Email: `doctor7@example.com`
   - Password: `password123`

8. **Dr. David Taylor** (Gynecology)
   - Email: `doctor8@example.com`
   - Password: `password123`

9. **Dr. Maria Thomas** (Psychiatry)
   - Email: `doctor9@example.com`
   - Password: `password123`

10. **Dr. Christopher Lee** (Oncology)
    - Email: `doctor10@example.com`
    - Password: `password123`

## Other Test Accounts

### Patient Account
- Email: `patient@example.com`
- Password: `password123`

### Admin Account
- Email: `admin@example.com`
- Password: `password123`

## Notes

- All accounts are created automatically when the application starts (via DataLoader)
- Each doctor has 780 time slots generated (30 days × 26 slots per day)
- Slots are from 8:00 AM to 4:00 PM (30-minute intervals)
- All doctors work Monday through Saturday

## How to Change Hero Image

To change the landing page hero image, edit `/src/main/resources/templates/index.html`:

Find this line (around line 61):
```html
src="https://images.unsplash.com/photo-1576091160399-112ba8d25d1f?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80"
```

Replace with:
1. **Another Unsplash image**: Go to https://unsplash.com, search for "doctor" or "healthcare", click an image, right-click and copy image URL
2. **Your own image**: Upload to `/src/main/resources/static/images/` and use `/images/your-image.jpg`

Recommended image specs:
- Aspect ratio: 16:9 or 4:3
- Resolution: At least 1200px wide
- Format: JPG or PNG
- Theme: Professional healthcare, doctors, medical consultation

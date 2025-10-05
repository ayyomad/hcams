# Quick Start Guide - Hospital Appointment Management System

## 🚀 Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.9+
- MySQL 8.0+

### Database Setup
```bash
# 1. Login to MySQL
mysql -u root -p

# 2. Run the database creation script
source create_database.sql

# OR manually create:
CREATE DATABASE hospital_mvp_new;
CREATE USER 'hospital_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON hospital_mvp_new.* TO 'hospital_user'@'localhost';
FLUSH PRIVILEGES;
```

### Start Application
```bash
# Method 1: Using Maven
mvn spring-boot:run

# Method 2: Build and run JAR
mvn clean package -DskipTests
java -jar target/hospital-mvp-0.0.1-SNAPSHOT.jar
```

Visit: **http://localhost:8080**

---

## 👥 Test Credentials

### Patients
- **Email**: `patient@example.com`
- **Password**: `password123`

### Doctors (10 accounts)
| Email | Specialty | Password |
|-------|-----------|----------|
| doctor1@example.com | General Medicine | password123 |
| doctor2@example.com | Cardiology | password123 |
| doctor3@example.com | Dermatology | password123 |
| doctor4@example.com | Pediatrics | password123 |
| doctor5@example.com | Orthopedics | password123 |
| doctor6@example.com | Neurology | password123 |
| doctor7@example.com | ENT | password123 |
| doctor8@example.com | Gynecology | password123 |
| doctor9@example.com | Psychiatry | password123 |
| doctor10@example.com | Oncology | password123 |

### Admin
- **Email**: `admin@example.com`
- **Password**: `password123`

---

## 📝 User Workflows

### Patient Workflow
1. **Sign Up**
   - Click "Login / Sign Up"
   - Switch to "Sign Up" tab
   - Enter: Name, Email, Phone, Password
   - Click "Create Account"

2. **Login**
   - Email: Your registered email
   - Password: Your password
   - Role: Patient
   - Click "Login"

3. **Book Appointment**
   - Search doctors by name or specialty
   - Filter by specialty dropdown
   - Click on a doctor card
   - Select a date
   - Click "Load Slots"
   - Click on a **green** slot (available)
   - Confirm booking

4. **View Profile**
   - Click "My Profile" in navbar
   - See upcoming appointments
   - See past appointments
   - View medical reports

### Doctor Workflow
1. **Login**
   - Email: `doctor1@example.com`
   - Password: `password123`
   - Role: Doctor
   - Click "Login"

2. **View Dashboard**
   - See statistics (total, today, upcoming, completed)
   - View today's appointments
   - View all appointments

3. **Manage Slots**
   - Select a date
   - Click "Load Slots"
   - View your schedule (green = available, red = booked)

4. **Add Medical Notes**
   - Find an appointment
   - Click to add remarks/prescription
   - Mark as complete

### Admin Workflow
1. **Login**
   - Email: `admin@example.com`
   - Password: `password123`
   - Role: Admin
   - Click "Login"

2. **View Analytics**
   - Total patients
   - Total doctors
   - Total appointments
   - Today's appointments

---

## 🎨 UI Features

### Color Coding
- **🟢 Green Slots**: Available (you can book)
- **🔴 Red Slots**: Booked (disabled)
- **Blue Buttons**: Primary actions
- **Gray Buttons**: Secondary actions

### Navigation
- **Home**: Return to landing page (stays on home if clicked from dashboard)
- **My Profile**: View your profile and appointments
- **Logout**: Clear session and logout

### Session Management
- Login persists for **30 days**
- Works across browser tabs
- Stored in both localStorage and cookies
- Auto-redirects to dashboard if already logged in

---

## 🐛 Troubleshooting

### Issue: "Access Denied" on /profile
**Solution**: Build succeeded - restart the application
```bash
mvn clean spring-boot:run
```

### Issue: Slots not showing colors
**Solution**: Hard refresh the browser
- Chrome/Firefox: `Ctrl + Shift + R` (Windows/Linux)
- Mac: `Cmd + Shift + R`

### Issue: Doctor appointments not loading
**Solution**: 
1. Make sure you're logged in as a doctor
2. Check browser console for errors (F12)
3. Verify doctor ID is being extracted from token

### Issue: "Connection refused" to MySQL
**Solution**:
1. Make sure MySQL is running
2. Check credentials in `application.properties`
3. Verify database `hospital_mvp_new` exists

### Issue: Compilation errors
**Solution**:
```bash
mvn clean compile
# If errors persist, check Java version
java -version  # Should be 17+
```

---

## 📁 Project Structure

```
java-project/
├── src/
│   ├── main/
│   │   ├── java/com/example/hospitalmvp/
│   │   │   ├── controller/      # REST API endpoints
│   │   │   ├── service/         # Business logic
│   │   │   ├── repository/      # Database access
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── security/        # JWT & auth
│   │   │   └── dto/             # Data transfer objects
│   │   └── resources/
│   │       ├── static/          # CSS, JS
│   │       ├── templates/       # HTML pages
│   │       └── application.properties
│   └── test/                    # Test files
├── pom.xml                      # Maven dependencies
├── REPORT.md                    # Detailed project report
├── FIXES_SUMMARY.md             # All fixes documented
├── DOCTOR_CREDENTIALS.md        # Login credentials
└── QUICK_START.md              # This file
```

---

## 🔧 Configuration

### Database Configuration
File: `src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_mvp_new
spring.datasource.username=hospital_user
spring.datasource.password=password123
```

### JWT Configuration
- **Token Expiry**: 7 days (default) or 30 days (remember me)
- **Secret Key**: Generated automatically
- **Algorithm**: HS256

### Server Configuration
- **Port**: 8080
- **Context Path**: /
- **Session**: Stateless (JWT-based)

---

## 📊 API Endpoints

### Public Endpoints (No Auth Required)
```
GET  /api/public/doctors           - List all doctors
GET  /api/public/doctors/{id}/slots?date=YYYY-MM-DD - Get doctor slots
POST /api/auth/login               - Login
POST /api/auth/register/patient    - Register patient
```

### Patient Endpoints (Requires PATIENT role)
```
GET    /api/patient/appointments/{patientId} - My appointments
POST   /api/patient/appointments              - Book appointment
DELETE /api/patient/appointments/{id}         - Cancel appointment
```

### Doctor Endpoints (Requires DOCTOR role)
```
GET   /api/doctor/appointments/{doctorId}     - My appointments
GET   /api/doctor/slots/{doctorId}            - My slots
PATCH /api/doctor/slots/{id}/availability     - Toggle slot
PATCH /api/doctor/appointments/{id}/remarks   - Add notes/prescription
```

### Admin Endpoints (Requires ADMIN role)
```
GET /api/admin/patients     - All patients
GET /api/admin/doctors      - All doctors
GET /api/admin/appointments - All appointments
```

---

## 🎯 Testing Checklist

- [ ] Patient can register
- [ ] Patient can login
- [ ] Patient can search doctors
- [ ] Patient can see green (available) and red (booked) slots
- [ ] Patient can book an appointment
- [ ] Patient can view profile
- [ ] Patient can cancel appointment
- [ ] Doctor can login
- [ ] Doctor can see appointments
- [ ] Doctor can view slots
- [ ] Doctor can add prescription
- [ ] Admin can login
- [ ] Admin can see analytics
- [ ] Session persists across navigation
- [ ] Logout clears session

---

## 🆘 Getting Help

### Error Messages
All API errors return JSON:
```json
{
  "message": "Error description here"
}
```

### Browser Console
Press **F12** to open developer tools and check:
- **Console tab**: JavaScript errors
- **Network tab**: API request/response
- **Application tab**: LocalStorage and cookies

### Logs
Check application logs for backend errors:
```bash
# If running with Maven
# Logs appear in terminal

# If running as JAR
java -jar target/hospital-mvp-0.0.1-SNAPSHOT.jar > app.log 2>&1
```

---

## 📚 Documentation

- **REPORT.md**: Complete technical documentation (75+ pages)
- **FIXES_SUMMARY.md**: All bugs fixed and how
- **DOCTOR_CREDENTIALS.md**: All login credentials + hero image guide
- **WORKFLOW_IMPROVEMENTS.md**: Feature improvements made

---

## ✅ Build Status

```
[INFO] BUILD SUCCESS
[INFO] Total time:  11.128 s
```

**All systems ready!** 🎉

---

**Created**: October 2024  
**Last Updated**: October 5, 2024  
**Version**: 1.0.0  
**Status**: ✅ Production Ready

# 🚀 Start Application - Quick Guide

## ✅ Fixed Issues
1. **OtpVerificationRepository** - Added missing `@Modifying` and `@Transactional` annotations
2. **DataLoader Foreign Key Constraint** - Fixed delete order (appointments deleted before slots)
3. **Build Status** - ✅ BUILD SUCCESS (all compilation errors fixed)

---

## 🎯 Steps to Run the Application

### Step 1: Ensure MySQL is Running

First, check if MySQL is installed and running on your system:

```powershell
# Check MySQL status
mysql --version
```

### Step 2: Create Database and User

Run ONE of these options:

#### Option A: Using MySQL Command Line (Recommended)

```powershell
# Login to MySQL (you'll need your MySQL root password)
mysql -u root -p

# Then run:
source D:/Common/Downloads/java-project/create_database.sql

# Or manually run these commands:
CREATE DATABASE IF NOT EXISTS hospital_mvp_new;
CREATE USER IF NOT EXISTS 'hospital_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON hospital_mvp_new.* TO 'hospital_user'@'localhost';
FLUSH PRIVILEGES;
exit
```

#### Option B: Using MySQL Workbench
1. Open MySQL Workbench
2. Connect as root user
3. Open the file `create_database.sql` 
4. Execute the script

### Step 3: Start the Application

```powershell
# Navigate to project directory
cd D:\Common\Downloads\java-project

# Start the application
mvn spring-boot:run
```

**Wait for this message in the console:**
```
🎉 Database seeding completed successfully!
```

### Step 4: Access the Application

Open your browser and go to:
**http://localhost:8080**

---

## 👥 Login Credentials

### Test Accounts:

| Role | Email | Password |
|------|-------|----------|
| **Patient** | patient@example.com | password123 |
| **Doctor 1** | doctor1@example.com | password123 |
| **Doctor 2** | doctor2@example.com | password123 |
| **Doctor 3** | doctor3@example.com | password123 |
| **Doctor 4** | doctor4@example.com | password123 |
| **Doctor 5** | doctor5@example.com | password123 |
| **Doctor 6** | doctor6@example.com | password123 |
| **Doctor 7** | doctor7@example.com | password123 |
| **Doctor 8** | doctor8@example.com | password123 |
| **Doctor 9** | doctor9@example.com | password123 |
| **Doctor 10** | doctor10@example.com | password123 |
| **Admin** | admin@example.com | password123 |

---

## 🐛 Troubleshooting

### Problem: MySQL Connection Error

**Error Message:** 
```
Access denied for user 'hospital_user'@'localhost'
```

**Solution:**
```sql
-- Login as root and run:
GRANT ALL PRIVILEGES ON hospital_mvp_new.* TO 'hospital_user'@'localhost';
FLUSH PRIVILEGES;
```

### Problem: Database doesn't exist

**Error Message:**
```
Unknown database 'hospital_mvp_new'
```

**Solution:**
```sql
CREATE DATABASE hospital_mvp_new;
```

### Problem: Port 8080 already in use

**Error Message:**
```
Port 8080 was already in use
```

**Solution:**
```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F

# OR change the port in application.properties:
# server.port=8081
```

### Problem: Build fails

**Solution:**
```powershell
# Clean and rebuild
mvn clean install -DskipTests
```

### Problem: Java version error

**Solution:**
```powershell
# Check Java version (needs Java 17+)
java -version

# If version is less than 17, install Java 17 or higher
```

---

## 📋 What Happens on Startup?

The application automatically:
1. ✅ Creates database tables (users, patients, doctors, admins, appointments, slots)
2. ✅ Seeds the database with test data:
   - 1 patient account
   - 10 doctor accounts with different specialties
   - 1 admin account
   - ~7,800 time slots (780 per doctor for next 30 days)

---

## 🔍 Verify Everything is Working

### Check 1: Application Started
Look for these messages in console:
```
Started HospitalMvpApplication in X seconds
```

### Check 2: Database Seeded
Look for:
```
✅ Created patient: John Patient
✅ Created doctor: Dr. Sarah Johnson (General Medicine)
📅 Generated 780 slots for Dr. Sarah Johnson
...
🎉 Database seeding completed successfully!
```

### Check 3: Web Access
1. Open http://localhost:8080
2. You should see the landing page
3. Click "Login / Sign Up"
4. Try logging in with: patient@example.com / password123

---

## 🎯 Quick Test Workflow

1. **Login as Patient**: patient@example.com / password123
2. **Search for a doctor** on the dashboard
3. **Click on a doctor** to view available slots
4. **Book an appointment** (green slots are available)
5. **View your appointments** in the profile section

---

## 📚 Additional Documentation

- **QUICK_START.md** - Detailed user workflows
- **SETUP_INSTRUCTIONS.md** - Database setup details
- **FIXES_SUMMARY.md** - All fixes and features
- **DOCTOR_CREDENTIALS.md** - Complete list of test accounts
- **EMAIL_OTP_SETUP.md** - Email OTP configuration (optional)

---

## ✅ Build Status

```
[INFO] BUILD SUCCESS
[INFO] Total time:  8.097 s
[INFO] Compilation errors: 0
```

**Application is ready to run!** 🎉

---

## 💡 Tips

1. **Keep MySQL running** while using the application
2. **Use Chrome or Firefox** for best experience
3. **Press F12** to open browser console if you encounter issues
4. **Session persists for 30 days** - you won't need to login again
5. **Hard refresh (Ctrl+Shift+R)** if UI doesn't update

---

## 🆘 Still Having Issues?

1. Check if MySQL is running:
   ```powershell
   mysql -u hospital_user -ppassword123 -e "SHOW DATABASES;"
   ```

2. Check if database exists:
   ```powershell
   mysql -u hospital_user -ppassword123 hospital_mvp_new -e "SHOW TABLES;"
   ```

3. View application logs in the terminal where you ran `mvn spring-boot:run`

4. Check the documentation files in the project root for more details

---

**Last Updated:** October 5, 2025  
**Status:** ✅ Ready to Run

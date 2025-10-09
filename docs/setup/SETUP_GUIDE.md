# HCAMS Setup Guide

Complete installation and setup instructions for HCAMS.

---

## Table of Contents
- [Prerequisites](#prerequisites)
- [Database Setup](#database-setup)
- [Application Setup](#application-setup)
- [Verification](#verification)
- [Troubleshooting](#troubleshooting)

---

## Prerequisites

### Required Software
- **Java 17+** - [Download](https://adoptium.net/)
- **Maven 3.9+** - [Download](https://maven.apache.org/download.cgi)
- **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/mysql/)

### Verify Installation

```bash
# Check Java
java -version

# Check Maven
mvn -version

# Check MySQL
mysql --version
```

---

## Database Setup

### Option 1: Automated Script (Recommended)

```bash
# Run the database creation script
mysql -u root -p < create_database.sql
```

### Option 2: Manual Setup

```sql
-- Connect to MySQL
mysql -u root -p

-- Create database
CREATE DATABASE IF NOT EXISTS hospital_mvp_new;

-- Create user
CREATE USER IF NOT EXISTS 'hospital_user'@'localhost' IDENTIFIED BY 'password123';

-- Grant privileges
GRANT ALL PRIVILEGES ON hospital_mvp_new.* TO 'hospital_user'@'localhost';
FLUSH PRIVILEGES;

-- Exit
exit
```

### Verify Database Setup

```bash
# Login as hospital_user
mysql -u hospital_user -ppassword123

# Check database
SHOW DATABASES;
USE hospital_mvp_new;

# Exit
exit
```

---

## Application Setup

### 1. Clone/Download Project

```bash
cd /path/to/your/workspace
# If using git
git clone https://github.com/ayyomad/hcams.git
cd hcams
```

### 2. Configure Application

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_mvp_new
spring.datasource.username=hospital_user
spring.datasource.password=password123

# Server Port (change if 8080 is in use)
server.port=8080
```

### 3. Build Application

```bash
# Clean and compile
mvn clean compile

# Or build JAR file
mvn clean package -DskipTests
```

### 4. Run Application

**Option A: Using Maven**
```bash
mvn spring-boot:run
```

**Option B: Using JAR**
```bash
java -jar target/hospital-mvp-0.0.1-SNAPSHOT.jar
```

**Option C: Windows Batch File**
```bash
# Double-click run.bat
# Or run from command line:
run.bat
```

---

## Verification

### 1. Check Console Output

You should see:
```
🌱 Starting database seeding...
✅ Created patient: John Patient
✅ Created doctor: Dr. Sarah Johnson (General Medicine)
📅 Generated 780 slots for Dr. Sarah Johnson
...
🎉 Database seeding completed successfully!

Started HospitalMvpApplication in X.XXX seconds
```

### 2. Access Application

Open browser and navigate to:
```
http://localhost:8080
```

### 3. Test Login

| Role | Email | Password |
|------|-------|----------|
| Patient | patient@example.com | password123 |
| Doctor | doctor1@example.com | password123 |
| Admin | admin@example.com | password123 |

---

## Troubleshooting

### Issue: Port 8080 Already in Use

**Solution**: Change port in `application.properties`
```properties
server.port=8081
```

### Issue: MySQL Connection Error

**Symptoms**:
```
Access denied for user 'hospital_user'@'localhost'
```

**Solution**:
```sql
-- Re-grant privileges
mysql -u root -p
GRANT ALL PRIVILEGES ON hospital_mvp_new.* TO 'hospital_user'@'localhost';
FLUSH PRIVILEGES;
```

### Issue: Database Not Found

**Symptoms**:
```
Unknown database 'hospital_mvp_new'
```

**Solution**:
```sql
CREATE DATABASE hospital_mvp_new;
```

### Issue: Build Fails

**Solution**:
```bash
# Clean and rebuild
mvn clean install -DskipTests

# Check Java version
java -version  # Should be 17+
```

### Issue: Foreign Key Constraint Error

**Symptoms**:
```
Cannot delete or update a parent row: a foreign key constraint fails
```

**Solution**: The database has old data. Drop and recreate:
```sql
DROP DATABASE hospital_mvp_new;
CREATE DATABASE hospital_mvp_new;
```

Then restart the application.

---

## Next Steps

- Read the [Quick Start Guide](../QUICK_START.md) for usage instructions
- Check the [Technical Guide](development/TECHNICAL_GUIDE.md) for development
- See [API Reference](development/API_REFERENCE.md) for API documentation

---

**Last Updated**: October 5, 2025  
**Version**: 1.0.0

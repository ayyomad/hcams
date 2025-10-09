# HCAMS - Batch Scripts Guide

Utility scripts for easy project management.

---

## 📁 Available Scripts

All batch files are located in the `scripts/` folder:

### **1. run.bat** (Root Directory)
**Location:** Project root  
**Purpose:** Quick start script

```batch
run.bat
```

**What it does:**
- Checks Java and Maven installation
- Starts Spring Boot application
- Opens on http://localhost:8080

**When to use:** Daily development

---

### **2. setup-database.bat**
**Location:** `scripts/setup-database.bat`

```batch
cd scripts
setup-database.bat
```

**What it does:**
- Creates MySQL database
- Creates database user
- Grants privileges
- Runs `create_database.sql`

**When to use:** First-time setup

**Requirements:**
- MySQL 8.0+ installed
- MySQL root password

---

### **3. build.bat**
**Location:** `scripts/build.bat`

```batch
cd scripts
build.bat
```

**What it does:**
- Cleans previous build
- Compiles Java code
- Packages into JAR file
- Skips tests for faster build

**Output:** `target/hospital-mvp-0.0.1-SNAPSHOT.jar`

**When to use:** 
- Creating deployment package
- Before production deployment
- After major code changes

---

### **4. clean.bat**
**Location:** `scripts/clean.bat`

```batch
cd scripts
clean.bat
```

**What it does:**
- Removes `target/` folder
- Deletes compiled classes
- Cleans Maven cache

**When to use:**
- Build issues
- Fresh compilation needed
- Disk space cleanup

---

### **5. test.bat**
**Location:** `scripts/test.bat`

```batch
cd scripts
test.bat
```

**What it does:**
- Runs all JUnit tests
- Displays test results
- Reports failures

**When to use:**
- Before committing code
- After bug fixes
- Quality assurance

---

## 🚀 Common Workflows

### **First Time Setup:**
```batch
# 1. Setup database
cd scripts
setup-database.bat

# 2. Build project
build.bat

# 3. Run application
cd ..
run.bat
```

---

### **Daily Development:**
```batch
# Just run the app
run.bat
```

---

### **Before Deployment:**
```batch
# 1. Run tests
cd scripts
test.bat

# 2. Clean build
clean.bat

# 3. Package
build.bat
```

---

### **Troubleshooting:**
```batch
# Clean everything and rebuild
cd scripts
clean.bat
build.bat

# Then try running
cd ..
run.bat
```

---

## ⚙️ Script Customization

### **Modify Database Credentials:**

Edit `scripts/setup-database.bat`:
```batch
mysql -u root -p < create_database.sql
```

### **Change Build Options:**

Edit `scripts/build.bat`:
```batch
# Add test execution
mvn clean package

# Skip specific tests
mvn clean package -DskipTests=false
```

### **Add Custom Scripts:**

Create new .bat file in `scripts/` folder:
```batch
@echo off
echo My Custom Script
cd /d "%~dp0\.."
# Your commands here
pause
```

---

## 🐛 Troubleshooting Scripts

### **Issue: "Java not found"**

**Solution:**
1. Install Java 17+
2. Add Java to PATH:
   ```
   System Properties → Environment Variables → Path
   Add: C:\Program Files\Java\jdk-17\bin
   ```

### **Issue: "Maven not found"**

**Solution:**
1. Install Maven 3.9+
2. Add Maven to PATH:
   ```
   Add: C:\Program Files\Apache\maven\bin
   ```

### **Issue: "MySQL command not found"**

**Solution:**
1. Add MySQL to PATH:
   ```
   Add: C:\Program Files\MySQL\MySQL Server 8.0\bin
   ```

### **Issue: "Access Denied" on database script**

**Solution:**
- Run command prompt as Administrator
- Or check MySQL root password

---

## 📋 Script Reference

| Script | Purpose | Requirements | Output |
|--------|---------|--------------|--------|
| run.bat | Start app | Java, Maven | Running server |
| setup-database.bat | Setup DB | MySQL | Database created |
| build.bat | Build JAR | Java, Maven | JAR file |
| clean.bat | Clean build | Maven | Clean workspace |
| test.bat | Run tests | Maven | Test results |

---

## 💡 Tips

1. **Run from Command Prompt:** Double-click or run from CMD
2. **Check Prerequisites:** Scripts validate Java/Maven
3. **Read Output:** Scripts show what they're doing
4. **Pause on Error:** Scripts wait before closing on error
5. **Logs:** Check console output for errors

---

## 🔗 Related Documentation

- [Setup Guide](setup/SETUP_GUIDE.md)
- [Quick Start](QUICK_START.md)
- [README](../README.md)

---

**Last Updated:** October 5, 2025  
**Version:** 1.0.0

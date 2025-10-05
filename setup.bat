setup.bat@echo off
echo 🏥 Hospital MVP Fresh Setup
echo ==========================

echo 🔍 Checking MySQL connection...
mysql -u root -p -e "SELECT 1;" >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ MySQL is not running or root access failed
    echo Please start MySQL and ensure root access is available
    pause
    exit /b 1
)

echo ✅ MySQL is running

echo 🗄️  Creating new database...
mysql -u root -p < create_database.sql

if %errorlevel% neq 0 (
    echo ❌ Database creation failed
    pause
    exit /b 1
)

echo ✅ Database created successfully

echo 🔗 Testing database connection...
mysql -u hospital_user -ppassword123 -e "USE hospital_mvp_new; SELECT 1;" >nul 2>&1

if %errorlevel% neq 0 (
    echo ❌ Database connection failed
    pause
    exit /b 1
)

echo ✅ Database connection successful
echo.
echo 🚀 Starting application...
echo The application will seed the database with sample data
echo.

mvn spring-boot:run
pause

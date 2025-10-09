@echo off
echo ========================================
echo HCAMS - Database Setup Script
echo ========================================
echo.

cd /d "%~dp0\.."

echo This script will setup the MySQL database for HCAMS.
echo.
echo Prerequisites:
echo - MySQL 8.0+ installed and running
echo - MySQL root password ready
echo.
pause

echo.
echo Running database creation script...
echo Please enter your MySQL root password when prompted.
echo.

mysql -u root -p < create_database.sql

if errorlevel 1 (
    echo.
    echo ERROR: Database setup failed!
    echo Please check:
    echo - MySQL is running
    echo - You entered the correct password
    echo - MySQL is accessible from command line
    pause
    exit /b 1
)

echo.
echo ========================================
echo Database setup completed successfully!
echo ========================================
echo.
echo Database created: hospital_mvp_new
echo User created: hospital_user
echo Password: password123
echo.
echo You can now run the application using run.bat
echo.
pause

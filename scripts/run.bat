@echo off
echo ========================================
echo HCAMS - Healthcare Appointment System
echo ========================================
echo.

cd /d "%~dp0"

echo Checking prerequisites...

java -version 2>&1 | findstr /i "version" >nul
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

mvn -version 2>&1 | findstr /i "Apache Maven" >nul
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.9+
    pause
    exit /b 1
)

echo.
echo ========================================
echo Starting HCAMS Application...
echo ========================================
echo.
echo The application will be available at:
echo http://localhost:8080
echo.
echo Press Ctrl+C to stop the application
echo.

mvn spring-boot:run

pause

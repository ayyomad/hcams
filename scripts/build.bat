@echo off
echo ========================================
echo HCAMS - Build Script
echo ========================================
echo.

cd /d "%~dp0\.."

echo Cleaning and building the project...
echo.

mvn clean package -DskipTests

if errorlevel 1 (
    echo.
    echo ERROR: Build failed!
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo ========================================
echo Build completed successfully!
echo ========================================
echo.
echo JAR file created at:
echo target\hospital-mvp-0.0.1-SNAPSHOT.jar
echo.
echo You can now run the application using run.bat
echo.
pause

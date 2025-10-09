@echo off
echo ========================================
echo HCAMS - Clean Build Script
echo ========================================
echo.

cd /d "%~dp0\.."

echo Cleaning project...
echo This will remove all compiled files and dependencies.
echo.
pause

mvn clean

if errorlevel 1 (
    echo.
    echo ERROR: Clean failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Clean completed successfully!
echo ========================================
echo.
pause

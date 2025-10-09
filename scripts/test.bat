@echo off
echo ========================================
echo HCAMS - Run Tests
echo ========================================
echo.

cd /d "%~dp0\.."

echo Running all tests...
echo.

mvn test

if errorlevel 1 (
    echo.
    echo ERROR: Some tests failed!
    echo Please check the test results above.
    pause
    exit /b 1
)

echo.
echo ========================================
echo All tests passed successfully!
echo ========================================
echo.
pause

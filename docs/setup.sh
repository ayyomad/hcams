#!/bin/bash

echo "🏥 Hospital MVP Fresh Setup"
echo "=========================="

# Check if MySQL is running
echo "🔍 Checking MySQL connection..."
mysql -u root -p -e "SELECT 1;" > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "❌ MySQL is not running or root access failed"
    echo "Please start MySQL and ensure root access is available"
    exit 1
fi

echo "✅ MySQL is running"

# Create database
echo "🗄️  Creating new database..."
mysql -u root -p < create_database.sql

if [ $? -eq 0 ]; then
    echo "✅ Database created successfully"
else
    echo "❌ Database creation failed"
    exit 1
fi

# Test database connection
echo "🔗 Testing database connection..."
mysql -u hospital_user -ppassword123 -e "USE hospital_mvp_new; SELECT 1;" > /dev/null 2>&1

if [ $? -eq 0 ]; then
    echo "✅ Database connection successful"
else
    echo "❌ Database connection failed"
    exit 1
fi

echo ""
echo "🚀 Starting application..."
echo "The application will seed the database with sample data"
echo ""

# Start the application
mvn spring-boot:run

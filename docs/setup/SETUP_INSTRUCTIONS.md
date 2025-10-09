# Hospital MVP Fresh Setup Instructions

## Step 1: Create New Database

### Option A: Using MySQL Command Line

```bash
# Connect to MySQL as root
mysql -u root -p

# Run the database creation script
source create_database.sql

# Exit MySQL
exit
```

### Option B: Using MySQL Workbench or phpMyAdmin

1. Open MySQL Workbench or phpMyAdmin
2. Connect as root user
3. Run the SQL commands from `create_database.sql`

## Step 2: Start the Application

```bash
# Make sure you're in the project directory
cd /path/to/java-project

# Start the application
mvn spring-boot:run
```

## Step 3: Verify Setup

### Check Console Output

You should see:

```
🌱 Starting database seeding...
✅ Created patient: John Patient
✅ Created doctor: Dr. Sarah Johnson (General Medicine)
   📅 Generated 780 slots for Dr. Sarah Johnson
✅ Created doctor: Dr. Michael Brown (Cardiology)
   📅 Generated 780 slots for Dr. Michael Brown
... (more doctors)
✅ Created admin: Super Admin
🎉 Database seeding completed successfully!
```

### Test the Application

1. Open browser: http://localhost:8080
2. Click "Login / Sign Up"
3. Login with:
   - **Patient**: patient@example.com / password123
   - **Doctor**: doctor1@example.com / password123
   - **Admin**: admin@example.com / password123

## Step 4: Verify Database Content

```sql
-- Connect to the new database
mysql -u hospital_user -ppassword123 hospital_mvp_new

-- Check tables were created
SHOW TABLES;

-- Check doctors were created
SELECT COUNT(*) FROM doctors;

-- Check slots were generated
SELECT COUNT(*) FROM slots;

-- Check a specific doctor's slots
SELECT COUNT(*) FROM slots WHERE doctor_id = 1;
```

## Troubleshooting

### If you get connection errors:

1. Make sure MySQL is running
2. Check if the user `hospital_user` exists and has proper permissions
3. Verify the database `hospital_mvp_new` was created

### If you get permission errors:

```sql
-- Grant permissions again
GRANT ALL PRIVILEGES ON hospital_mvp_new.* TO 'hospital_user'@'localhost';
FLUSH PRIVILEGES;
```

### If you want to start completely fresh:

1. Drop the database: `DROP DATABASE hospital_mvp_new;`
2. Run the setup script again
3. Restart the application

## Expected Results

- ✅ Database `hospital_mvp_new` created
- ✅ User `hospital_user` with password `password123` created
- ✅ 10 doctors with different specializations
- ✅ 1 patient user
- ✅ 1 admin user
- ✅ ~7800 total slots (780 per doctor for 30 days)
- ✅ Application running on http://localhost:8080

-- Hospital MVP Database Flush and Seed Script
-- WARNING: This will delete all existing data!

USE hospital_mvp;

-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Drop all tables in correct order (respecting foreign key constraints)
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS slots;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS doctors;
DROP TABLE IF EXISTS admins;
DROP TABLE IF EXISTS users;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Note: Tables will be recreated by JPA with ddl-auto=update
-- The application will automatically create the schema on startup

-- Insert seed data will be handled by the updated DataLoader.java
-- This script only flushes existing data

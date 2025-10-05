-- Create new Hospital MVP Database
-- Run this script to create a fresh database

-- Create database
CREATE DATABASE IF NOT EXISTS hospital_mvp_new;

-- Create user (if it doesn't exist)
CREATE USER IF NOT EXISTS 'hospital_user'@'localhost' IDENTIFIED BY 'password123';

-- Grant all privileges on the new database
GRANT ALL PRIVILEGES ON hospital_mvp_new.* TO 'hospital_user'@'localhost';

-- Flush privileges
FLUSH PRIVILEGES;

-- Use the new database
USE hospital_mvp_new;

-- The tables will be created automatically by JPA with ddl-auto=create
-- No need to create tables manually

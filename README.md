# HCAMS - Healthcare Appointment Management System

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)]()
[![Java](https://img.shields.io/badge/Java-17-orange)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-green)]()
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)]()

A modern, comprehensive web-based healthcare appointment management system with role-based access control for Patients, Doctors, and Administrators.

---

## 🚀 Quick Start

```bash
# 1. Setup database
mysql -u root -p < create_database.sql

# 2. Run application
mvn spring-boot:run

# 3. Open browser
http://localhost:8080
```

### Default Login Credentials

| Role | Email | Password |
|------|-------|----------|
| **Patient** | patient@example.com | password123 |
| **Doctor** | doctor1@example.com | password123 |
| **Admin** | admin@example.com | password123 |

---

## 📚 Documentation

- **[Setup Guide](docs/setup/SETUP_GUIDE.md)** - Complete installation and configuration
- **[User Guide](docs/QUICK_START.md)** - How to use the application
- **[Developer Guide](docs/development/TECHNICAL_GUIDE.md)** - Technical documentation
- **[API Documentation](docs/development/API_REFERENCE.md)** - REST API endpoints

---

## ✨ Features

### For Patients
- 🔍 Search and filter doctors by specialty
- 📅 Real-time appointment booking
- 📊 View appointment history
- 💳 Manage profile and medical records

### For Doctors
- 📋 Manage appointment schedule
- 👥 View patient appointments
- 📝 Add medical notes and prescriptions
- 📊 Dashboard with statistics

### For Administrators
- 👨‍⚕️ Manage doctors and patients
- 📈 View system analytics
- 🔐 User access control
- 📊 Generate reports

---

## 🛠️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.3.3
- **Frontend**: HTML5, CSS3, JavaScript
- **Database**: MySQL 8.0
- **Security**: JWT Authentication, Spring Security
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven 3.9+

---

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.9+
- MySQL 8.0+
- 4GB RAM minimum
- Modern web browser (Chrome, Firefox, Safari, Edge)

---

## 🔧 Configuration

The application uses `application.properties` for configuration:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_mvp_new
spring.datasource.username=hospital_user
spring.datasource.password=password123

# Server
server.port=8080

# JWT
app.security.jwt.secret=<your-secret-key>
app.security.jwt.expiration-ms=86400000
```

---

## 🏗️ Project Structure

```
hcams/
├── src/
│   ├── main/
│   │   ├── java/com/example/hospitalmvp/
│   │   │   ├── controller/      # REST Controllers
│   │   │   ├── service/         # Business Logic
│   │   │   ├── repository/      # Data Access
│   │   │   ├── entity/          # JPA Entities
│   │   │   ├── security/        # Security & JWT
│   │   │   └── dto/             # Data Transfer Objects
│   │   └── resources/
│   │       ├── static/          # CSS, JS, Images
│   │       ├── templates/       # HTML Templates
│   │       └── application.properties
│   └── test/                    # Unit Tests
├── docs/                        # Documentation
├── pom.xml                      # Maven Configuration
└── README.md                    # This file
```

---




**Last Updated**: October 5, 2025  
**Version**: 1.0.0  
**Status**: ✅ Production Ready

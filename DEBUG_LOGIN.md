# Login Debugging Guide

## The Issue

You're getting "Bad credentials" when trying to login with `patient@example.com` and `password123`.

## Debugging Steps

### 1. First, restart the application to ensure fresh data seeding

```bash
mvn spring-boot:run
```

### 2. Check the console output for seeding messages

You should see:

```
🌱 Starting database seeding...
🔐 Password encoder test: $2a$10$...
✅ Created patient: John Patient (ID: 1)
   Email: patient@example.com
   Password hash: $2a$10$...
   Enabled: true
```

### 3. Test if the user exists in the database

```bash
curl http://localhost:8080/api/test/user/patient@example.com
```

Expected response:

```json
{
  "id": 1,
  "email": "patient@example.com",
  "fullName": "John Patient",
  "role": "PATIENT",
  "enabled": true,
  "passwordHash": "$2a$10$..."
}
```

### 4. Test password verification

```bash
curl -X POST http://localhost:8080/api/test/verify-password \
  -H "Content-Type: application/json" \
  -d '{"email":"patient@example.com","password":"password123"}'
```

Expected response:

```json
{
  "success": true,
  "email": "patient@example.com",
  "userExists": true,
  "passwordMatches": true,
  "userEnabled": true,
  "userRole": "PATIENT"
}
```

### 5. Check all users

```bash
curl http://localhost:8080/api/test/all-users
```

### 6. Test login API directly

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"patient@example.com","password":"password123"}'
```

Expected response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## Common Issues and Solutions

### Issue 1: User not created

- **Symptom**: GET `/api/test/user/patient@example.com` returns 404
- **Solution**: Restart application and check console for seeding messages

### Issue 2: Password doesn't match

- **Symptom**: POST `/api/test/verify-password` returns `"passwordMatches": false`
- **Solution**: Check if you're using the exact password `password123` (no extra spaces)

### Issue 3: User disabled

- **Symptom**: `"userEnabled": false`
- **Solution**: Check DataLoader code - user should be enabled by default

### Issue 4: Wrong email

- **Symptom**: User not found
- **Solution**: Use exact email `patient@example.com` (case sensitive)

## Quick Fix Commands

If nothing works, try this sequence:

```bash
# 1. Stop the application (Ctrl+C)

# 2. Clear any existing database data
mysql -u hospital_user -ppassword123 hospital_mvp_new -e "DROP DATABASE hospital_mvp_new; CREATE DATABASE hospital_mvp_new;"

# 3. Start fresh
mvn spring-boot:run

# 4. Wait for seeding to complete, then test
curl http://localhost:8080/api/test/user/patient@example.com
```

## Expected Working Flow

1. ✅ Application starts
2. ✅ DataLoader runs and creates users
3. ✅ Console shows successful seeding
4. ✅ GET `/api/test/user/patient@example.com` returns user info
5. ✅ POST `/api/test/verify-password` returns `"passwordMatches": true`
6. ✅ POST `/api/auth/login` returns JWT token
7. ✅ Frontend login works

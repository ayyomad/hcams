# HCAMS Live Demo Guide

## 🎯 Complete Demo Walkthrough for Judges

---

## **PART 1: Application Demo (Running App)**

### Pre-Demo Setup
```batch
1. Start MySQL (must be running)
2. Run: run.bat
3. Wait for: "Started HospitalMvpApplication"
4. Open browser: http://localhost:8080
5. Keep this tab open
```

---

### Demo Script

#### **Step 1: Show Landing Page** (30 seconds)
```
Point out:
✓ Modern UI with light gradient
✓ Statistics cards (500+ Doctors, 10k+ Patients)
✓ Specialty cards with icons
✓ "Book Now" buttons
```

**What to say:**
"This is our landing page. Notice the modern design with real-time statistics and specialty categories."

---

#### **Step 2: Patient Login & Booking** (2 minutes)

**Login:**
```
1. Click "Login / Sign Up"
2. Email: patient@example.com
3. Password: password123
4. Role: Patient
5. Click "Login"
```

**Book Appointment:**
```
1. Dashboard loads → "Welcome back, John Patient"
2. Search bar → Type "Cardiology"
3. Click on "Dr. Sarah Johnson - Cardiology"
4. Modal opens with calendar
5. Select today's date
6. Show slots: Green (available) vs Red (booked)
7. Click a GREEN slot
8. Confirm booking
9. Success message appears
```

**What to say:**
"As a patient, I can search doctors by specialty, see real-time availability, and book instantly. Green slots are available, red are booked."

---

#### **Step 3: Doctor View** (1.5 minutes)

**Logout & Login as Doctor:**
```
1. Click "Logout"
2. Login again
3. Email: doctor1@example.com
4. Password: password123
5. Role: Doctor
6. Click "Login"
```

**Show Doctor Dashboard:**
```
1. Point to statistics: Total appointments, Today's appointments
2. Scroll to "Today's Appointments" section
3. Find the appointment just booked
4. Show patient name, time slot
5. Click "Add Prescription" or "Add Notes"
6. Type: "Blood pressure check recommended"
7. Save
```

**What to say:**
"Doctors see their schedule, today's appointments, and can add prescriptions instantly."

---

#### **Step 4: Admin View** (1 minute)

**Login as Admin:**
```
1. Logout
2. Email: admin@example.com
3. Password: password123
4. Role: Admin
5. Click "Login"
```

**Show Admin Dashboard:**
```
1. Point to system statistics
2. Total Patients: X
3. Total Doctors: 10
4. Total Appointments: X
5. Scroll to "All Appointments" table
6. Show our booked appointment in the list
```

**What to say:**
"Admins have full system visibility - all doctors, patients, and appointments in one place."

---

## **PART 2: MySQL Workbench Demo (Database)**

### Open MySQL Workbench
```
1. Open MySQL Workbench
2. Connect to Local instance
3. Select database: hospital_mvp_new
```

---

### Demo Script

#### **Step 1: Show Tables** (30 seconds)

```sql
SHOW TABLES;
```

**What to say:**
"Our database has 7 main tables. Notice the inheritance structure - users table is parent, patients and doctors are children."

**Point out:**
```
✓ users (parent table)
✓ patients (child)
✓ doctors (child)
✓ admins (child)
✓ appointments (core functionality)
✓ slots (time management)
✓ otp_verification (security)
```

---

#### **Step 2: Show Users Table** (30 seconds)

```sql
SELECT 
    id, 
    email, 
    full_name, 
    role 
FROM users 
ORDER BY role;
```

**What to say:**
"The users table stores all users. Role column determines if they're PATIENT, DOCTOR, or ADMIN. This demonstrates our role-based security."

**Point out:**
- Different roles in one table
- Email uniqueness
- Clean data structure

---

#### **Step 3: Show Doctors with Specialty** (30 seconds)

```sql
SELECT 
    d.id,
    u.full_name,
    d.specialty,
    d.rating
FROM doctors d
JOIN users u ON d.id = u.id
ORDER BY d.specialty;
```

**What to say:**
"This JOIN query demonstrates our inheritance. Doctors table has specialty info, users table has name. We JOIN them to get complete information."

**Point out:**
- Table inheritance (JOINED strategy)
- Foreign key relationship
- Different specialties

---

#### **Step 4: Show Today's Appointments** (45 seconds)

```sql
SELECT 
    a.id AS appointment_id,
    p_user.full_name AS patient_name,
    d_user.full_name AS doctor_name,
    d.specialty,
    a.appointment_date,
    a.start_time,
    a.status
FROM appointments a
JOIN patients p ON a.patient_id = p.id
JOIN users p_user ON p.id = p_user.id
JOIN doctors d ON a.doctor_id = d.id
JOIN users d_user ON d.id = d_user.id
WHERE a.appointment_date = CURDATE()
ORDER BY a.start_time;
```

**What to say:**
"This complex query shows today's appointments with patient names, doctor names, and specialties. Notice we're JOINing 5 tables - this demonstrates database relationships and SQL skills."

**Point out:**
- Multiple JOINs
- Foreign key relationships
- WHERE clause filtering
- Real appointment data

---

#### **Step 5: Show Slots Availability** (30 seconds)

```sql
SELECT 
    s.id,
    u.full_name AS doctor_name,
    s.start_time,
    s.end_time,
    s.available,
    s.version
FROM slots s
JOIN doctors d ON s.doctor_id = d.id
JOIN users u ON d.id = u.id
WHERE s.doctor_id = 1
  AND s.start_time >= NOW()
LIMIT 10;
```

**What to say:**
"Slots table manages availability. The 'version' column implements optimistic locking - prevents two patients from booking the same slot simultaneously."

**Point out:**
- available = TRUE (green in UI)
- available = FALSE (red in UI)
- version column (concurrency control)

---

#### **Step 6: Demonstrate Data Integrity** (30 seconds)

```sql
-- Show foreign key constraint
SELECT 
    a.id AS appointment_id,
    a.slot_id,
    s.id AS slot_exists
FROM appointments a
LEFT JOIN slots s ON a.slot_id = s.id
WHERE s.id IS NULL;
```

**Expected Result:** 0 rows

**What to say:**
"This query checks data integrity. Zero rows means every appointment has a valid slot - our foreign keys are working correctly."

---

## **PART 3: Java Code Demo (IDE)**

### Open in VS Code / IntelliJ

---

### **Step 1: Show OOP - Inheritance** (1 minute)

**File:** `src/main/java/com/example/hospitalmvp/entity/user/User.java`

**Show:**
```java
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    private String password;
    private String fullName;
    
    @Enumerated(EnumType.STRING)
    private Role role;
}
```

**What to say:**
"User is the parent class. Notice @Inheritance annotation - this is JOINED inheritance strategy we saw in the database."

---

**File:** `src/main/java/com/example/hospitalmvp/entity/user/Doctor.java`

**Show:**
```java
@Entity
@Table(name = "doctors")
public class Doctor extends User {
    private String specialty;
    private BigDecimal rating;
    
    // Doctor IS-A User (Inheritance)
}
```

**What to say:**
"Doctor extends User - this is OOP inheritance. Doctor IS-A User, so it gets email, password, name automatically."

---

### **Step 2: Show Encapsulation** (30 seconds)

**File:** Same `User.java`

**Show:**
```java
private String password;  // Private field

public void setPassword(String password) {
    this.password = encoder.encode(password);  // Encrypted
}

public String getPassword() {
    return password;
}
```

**What to say:**
"Encapsulation - password is private. We control access through getters/setters. Password is encrypted before storing."

---

### **Step 3: Show Relationships** (45 seconds)

**File:** `src/main/java/com/example/hospitalmvp/entity/Appointment.java`

**Show:**
```java
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne  // Many appointments → One patient
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    @ManyToOne  // Many appointments → One doctor
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    
    @ManyToOne  // Many appointments → One slot
    @JoinColumn(name = "slot_id")
    private Slot slot;
}
```

**What to say:**
"Appointment demonstrates composition - it HAS-A patient, HAS-A doctor, HAS-A slot. @ManyToOne creates foreign keys we saw in database."

---

### **Step 4: Show Spring Boot Layers** (1 minute)

#### **Repository:**
**File:** `src/main/java/com/example/hospitalmvp/repository/AppointmentRepository.java`

```java
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByPatientId(Long patientId);
    
    List<Appointment> findByDoctorId(Long doctorId);
}
```

**What to say:**
"Repository layer - Spring Data JPA automatically converts method names to SQL. findByPatientId becomes SELECT * FROM appointments WHERE patient_id = ?"

---

#### **Service:**
**File:** `src/main/java/com/example/hospitalmvp/service/AppointmentService.java`

```java
@Service
@Transactional
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    
    public Long book(BookRequest request) {
        // Business logic
        Slot slot = slotRepository.findById(request.getSlotId())
            .orElseThrow();
            
        if (!slot.isAvailable()) {
            throw new RuntimeException("Slot not available");
        }
        
        // Save appointment
        Appointment appointment = appointmentRepository.save(appointment);
        
        // Mark slot unavailable
        slot.setAvailable(false);
        slotRepository.save(slot);
        
        return appointment.getId();
    }
}
```

**What to say:**
"Service layer has business logic. @Transactional ensures both appointment creation AND slot update happen together - or both fail. This prevents partial bookings."

---

#### **Controller:**
**File:** `src/main/java/com/example/hospitalmvp/controller/PatientController.java`

```java
@RestController
@RequestMapping("/api/patient")
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {
    
    private final AppointmentService appointmentService;
    
    @PostMapping("/appointments")
    public ResponseEntity<Long> book(@RequestBody @Valid BookRequest request) {
        Long id = appointmentService.book(request);
        return ResponseEntity.ok(id);
    }
}
```

**What to say:**
"Controller handles HTTP requests. @PreAuthorize ensures only patients can book appointments - this is role-based security."

---

### **Step 5: Show Security** (45 seconds)

**File:** `src/main/java/com/example/hospitalmvp/security/SecurityConfig.java`

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/patient/**").hasRole("PATIENT")
                .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
            );
        return http.build();
    }
}
```

**What to say:**
"Security configuration - patients can only access /api/patient/*, doctors only /api/doctor/*. This implements role-based access control."

---

## **Quick Demo Checklist**

### Before Demo:
- [ ] MySQL running
- [ ] Application started (run.bat)
- [ ] Browser open to localhost:8080
- [ ] MySQL Workbench open
- [ ] VS Code/IntelliJ open
- [ ] Know login credentials

### Demo Order:
1. [ ] Landing page (30 sec)
2. [ ] Patient booking (2 min)
3. [ ] Doctor view (1.5 min)
4. [ ] Admin view (1 min)
5. [ ] MySQL tables (30 sec)
6. [ ] SQL queries (2 min)
7. [ ] Java code OOP (2 min)

### Total Time: ~10 minutes

---

## **Key Points to Emphasize**

### To Judges:
1. **"Real-time booking prevents double-booking"**
2. **"Role-based security - each user sees only their data"**
3. **"Database uses inheritance and foreign keys"**
4. **"OOP principles: inheritance, encapsulation, polymorphism"**
5. **"Transaction management ensures data integrity"**
6. **"Spring Boot follows MVC architecture"**

---

## **If Demo Fails - Backup Plan**

1. **Have screenshots ready** of each screen
2. **Show database in Workbench** (always works)
3. **Focus on code** - walk through files
4. **Explain architecture** from memory
5. **Show diagrams** if prepared

---

## **Files to Have Open**

### MySQL Workbench:
- Connected to hospital_mvp_new database

### VS Code/IntelliJ:
1. User.java
2. Doctor.java
3. Appointment.java
4. AppointmentRepository.java
5. AppointmentService.java
6. PatientController.java
7. SecurityConfig.java

### Browser:
- http://localhost:8080

---

**Practice this demo 2-3 times before presentation!**

**Good Luck!** 🎯

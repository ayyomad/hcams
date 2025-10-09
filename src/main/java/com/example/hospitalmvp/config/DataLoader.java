package com.example.hospitalmvp.config;

import com.example.hospitalmvp.entity.Role;
import com.example.hospitalmvp.entity.Slot;
import com.example.hospitalmvp.entity.user.Admin;
import com.example.hospitalmvp.entity.user.Doctor;
import com.example.hospitalmvp.entity.user.Patient;
import com.example.hospitalmvp.repository.AdminRepository;
import com.example.hospitalmvp.repository.DoctorRepository;
import com.example.hospitalmvp.repository.PatientRepository;
import com.example.hospitalmvp.repository.SlotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(PasswordEncoder encoder, PatientRepository patientRepo, DoctorRepository doctorRepo,
            AdminRepository adminRepo, SlotRepository slotRepo, com.example.hospitalmvp.repository.AppointmentRepository appointmentRepo) {
        return args -> {
            System.out.println("🌱 Starting database seeding...");
            System.out.println("🔐 Password encoder test: " + encoder.encode("password123"));

            // Clear existing data (if any) - DELETE IN CORRECT ORDER TO AVOID FK CONSTRAINTS
            appointmentRepo.deleteAll();  // Delete appointments first (child)
            slotRepo.deleteAll();          // Then slots (referenced by appointments)
            patientRepo.deleteAll();
            doctorRepo.deleteAll();
            adminRepo.deleteAll();

            // Create sample patient
            Patient patient = new Patient();
            patient.setEmail("patient@example.com");
            patient.setPassword(encoder.encode("password123"));
            patient.setFullName("John Patient");
            patient.setPhone("+10000000000");
            patient.setEnabled(true);
            patient.setRole(Role.PATIENT);
            patient = patientRepo.save(patient);
            System.out.println("✅ Created patient: " + patient.getFullName() + " (ID: " + patient.getId() + ")");
            System.out.println("   Email: " + patient.getEmail());
            System.out.println("   Password hash: " + patient.getPassword());
            System.out.println("   Enabled: " + patient.isEnabled());

            // Create 10 doctors with distinct specializations
            String[][] doctors = {
                    { "doctor1@example.com", "Dr. Sarah Johnson", "General Medicine" },
                    { "doctor2@example.com", "Dr. Michael Brown", "Cardiology" },
                    { "doctor3@example.com", "Dr. Emily Davis", "Dermatology" },
                    { "doctor4@example.com", "Dr. James Wilson", "Pediatrics" },
                    { "doctor5@example.com", "Dr. Lisa Anderson", "Orthopedics" },
                    { "doctor6@example.com", "Dr. Robert Taylor", "ENT" },
                    { "doctor7@example.com", "Dr. Jennifer Martinez", "Neurology" },
                    { "doctor8@example.com", "Dr. David Garcia", "Gynecology" },
                    { "doctor9@example.com", "Dr. Amanda White", "Endocrinology" },
                    { "doctor10@example.com", "Dr. Christopher Lee", "Psychiatry" }
            };

            for (String[] doctorInfo : doctors) {
                Doctor doctor = new Doctor();
                doctor.setEmail(doctorInfo[0]);
                doctor.setPassword(encoder.encode("password123"));
                doctor.setFullName(doctorInfo[1]);
                doctor.setSpecialty(doctorInfo[2]);
                doctor.setEnabled(true);
                doctor.setRole(Role.DOCTOR);
                Doctor savedDoctor = doctorRepo.save(doctor);
                System.out.println(
                        "✅ Created doctor: " + savedDoctor.getFullName() + " (" + savedDoctor.getSpecialty() + ")");

                // Generate slots for this doctor (8:00 AM to 4:00 PM, 30-minute slots,
                // Monday-Saturday)
                int slotCount = generateSlotsForDoctor(savedDoctor, slotRepo);
                System.out.println("   📅 Generated " + slotCount + " slots for " + savedDoctor.getFullName());
            }

            // Create admin
            Admin admin = new Admin();
            admin.setEmail("admin@example.com");
            admin.setPassword(encoder.encode("password123"));
            admin.setFullName("Super Admin");
            admin.setEnabled(true);
            admin.setRole(Role.ADMIN);
            admin = adminRepo.save(admin);
            System.out.println("✅ Created admin: " + admin.getFullName());

            System.out.println("🎉 Database seeding completed successfully!");
            System.out.println("📋 Login credentials:");
            System.out.println("   Patient: patient@example.com / password123");
            System.out.println("   Doctor: doctor1@example.com / password123");
            System.out.println("   Admin: admin@example.com / password123");
            System.out.println("");
            System.out.println("🔍 Verification queries:");
            System.out.println("   SELECT id, email, enabled, role FROM users WHERE email='patient@example.com';");
            System.out.println("   SELECT COUNT(*) FROM users;");
            System.out.println("   SELECT COUNT(*) FROM slots;");
        };
    }

    private int generateSlotsForDoctor(Doctor doctor, SlotRepository slotRepo) {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(30); // Generate 30 days of slots
        int slotCount = 0;

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            // Skip Sunday
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                continue;
            }

            // Generate slots from 8:00 AM to 4:00 PM (16:00) with 30-minute intervals
            LocalTime startTime = LocalTime.of(8, 0);
            LocalTime endTime = LocalTime.of(16, 0);

            while (startTime.isBefore(endTime)) {
                LocalTime slotEndTime = startTime.plusMinutes(30);

                Slot slot = new Slot();
                slot.setDoctor(doctor);
                slot.setStartTime(OffsetDateTime.of(date, startTime, ZoneOffset.UTC));
                slot.setEndTime(OffsetDateTime.of(date, slotEndTime, ZoneOffset.UTC));
                slot.setAvailable(true);
                slotRepo.save(slot);
                slotCount++;

                startTime = slotEndTime;
            }
        }
        return slotCount;
    }
}
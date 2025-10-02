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

import java.time.OffsetDateTime;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(PasswordEncoder encoder, PatientRepository patientRepo, DoctorRepository doctorRepo,
            AdminRepository adminRepo, SlotRepository slotRepo) {
        return args -> {
            if (patientRepo.count() == 0) {
                Patient p = new Patient();
                p.setEmail("patient@example.com");
                p.setPassword(encoder.encode("password123"));
                p.setFullName("John Patient");
                p.setPhone("+10000000000");
                p.setEnabled(true);
                p.setRole(Role.PATIENT);
                patientRepo.save(p);
            }

            Doctor doctor;
            if (doctorRepo.count() == 0) {
                String[][] docs = new String[][] {
                        { "doctor@example.com", "Dr. Alice", "General" },
                        { "cardio1@example.com", "Dr. Brown", "Cardiologist" },
                        { "derma1@example.com", "Dr. Clark", "Dermatologist" },
                        { "pedia1@example.com", "Dr. Diaz", "Pediatrician" },
                        { "ortho1@example.com", "Dr. Evans", "Orthopedic" },
                        { "neuro1@example.com", "Dr. Ford", "Neurologist" },
                        { "ent1@example.com", "Dr. Green", "ENT" },
                        { "gyn1@example.com", "Dr. Hall", "Gynecologist" },
                        { "uro1@example.com", "Dr. Irwin", "Urologist" },
                        { "oph1@example.com", "Dr. Jones", "Ophthalmologist" },
                        { "psy1@example.com", "Dr. King", "Psychiatrist" },
                        { "onco1@example.com", "Dr. Lee", "Oncologist" },
                        { "endo1@example.com", "Dr. Moore", "Endocrinologist" },
                        { "neph1@example.com", "Dr. Neal", "Nephrologist" },
                        { "gasto1@example.com", "Dr. Owen", "Gastroenterologist" }
                };
                Doctor first = null;
                for (String[] info : docs) {
                    Doctor d = new Doctor();
                    d.setEmail(info[0]);
                    d.setPassword(encoder.encode("password123"));
                    d.setFullName(info[1]);
                    d.setSpecialty(info[2]);
                    d.setEnabled(true);
                    d.setRole(Role.DOCTOR);
                    Doctor saved = doctorRepo.save(d);
                    if (first == null)
                        first = saved;
                }
                doctor = first;
            } else {
                doctor = doctorRepo.findAll().get(0);
            }

            if (adminRepo.count() == 0) {
                Admin a = new Admin();
                a.setEmail("admin@example.com");
                a.setPassword(encoder.encode("password123"));
                a.setFullName("Super Admin");
                a.setEnabled(true);
                a.setRole(Role.ADMIN);
                adminRepo.save(a);
            }

            if (slotRepo.count() == 0) {
                OffsetDateTime base = OffsetDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0)
                        .withNano(0);
                for (Doctor d : doctorRepo.findAll()) {
                    for (int i = 0; i < 4; i++) {
                        Slot s = new Slot();
                        s.setDoctor(d);
                        s.setStartTime(base.plusHours(i));
                        s.setEndTime(base.plusHours(i + 1));
                        s.setAvailable(true);
                        slotRepo.save(s);
                    }
                }
            }
        };
    }
}

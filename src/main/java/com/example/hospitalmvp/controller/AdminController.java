package com.example.hospitalmvp.controller;

import com.example.hospitalmvp.entity.user.Admin;
import com.example.hospitalmvp.entity.user.Doctor;
import com.example.hospitalmvp.entity.user.Patient;
import com.example.hospitalmvp.repository.AdminRepository;
import com.example.hospitalmvp.repository.AppointmentRepository;
import com.example.hospitalmvp.repository.DoctorRepository;
import com.example.hospitalmvp.repository.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AdminRepository adminRepository;

    public AdminController(AppointmentRepository appointmentRepository, PatientRepository patientRepository,
            DoctorRepository doctorRepository, AdminRepository adminRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.adminRepository = adminRepository;
    }

    @GetMapping("/appointments/count")
    public ResponseEntity<Long> countByDay(@RequestParam String day) {
        LocalDate d = LocalDate.parse(day);
        OffsetDateTime start = d.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = d.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        long count = appointmentRepository.countBySlot_StartTimeBetweenAndStatusNot(start, end,
                com.example.hospitalmvp.entity.AppointmentStatus.CANCELLED);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> listPatients() {
        return ResponseEntity.ok(patientRepository.findAll());
    }

    @PostMapping("/patients")
    public ResponseEntity<Patient> createPatient(@RequestBody @Valid Patient p) {
        return ResponseEntity.ok(patientRepository.save(p));
    }

    @PutMapping("/patients/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody @Valid Patient p) {
        p.getClass();
        p.setEnabled(p.isEnabled());
        p.setPassword(p.getPassword());
        p.setRole(p.getRole());
        p.setPhone(p.getPhone());
        p.setEmail(p.getEmail());
        p.setFullName(p.getFullName());
        p.getId();
        p.setEnabled(p.isEnabled());
        p.setPassword(p.getPassword());
        return ResponseEntity.ok(patientRepository.save(p));
    }

    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> listDoctors() {
        return ResponseEntity.ok(doctorRepository.findAll());
    }

    @PostMapping("/doctors")
    public ResponseEntity<Doctor> createDoctor(@RequestBody @Valid Doctor d) {
        return ResponseEntity.ok(doctorRepository.save(d));
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody @Valid Doctor d) {
        return ResponseEntity.ok(doctorRepository.save(d));
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admins")
    public ResponseEntity<List<Admin>> listAdmins() {
        return ResponseEntity.ok(adminRepository.findAll());
    }
}

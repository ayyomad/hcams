package com.example.hospitalmvp.integration;

import com.example.hospitalmvp.dto.AppointmentDtos;
import com.example.hospitalmvp.entity.Appointment;
import com.example.hospitalmvp.entity.AppointmentStatus;
import com.example.hospitalmvp.entity.Slot;
import com.example.hospitalmvp.entity.user.Doctor;
import com.example.hospitalmvp.entity.user.Patient;
import com.example.hospitalmvp.repository.AppointmentRepository;
import com.example.hospitalmvp.repository.DoctorRepository;
import com.example.hospitalmvp.repository.PatientRepository;
import com.example.hospitalmvp.repository.SlotRepository;
import com.example.hospitalmvp.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AppointmentIntegrationTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Patient patient;
    private Doctor doctor;
    private Slot slot;

    @BeforeEach
    void setUp() {
        // Create test patient
        patient = new Patient();
        patient.setEmail("testpatient@example.com");
        patient.setPassword("password123"); // Add missing password
        patient.setFullName("Test Patient");
        patient.setPhone("+1234567890");
        patient.setEnabled(true);
        patient = patientRepository.save(patient);

        // Create test doctor
        doctor = new Doctor();
        doctor.setEmail("testdoctor@example.com");
        doctor.setPassword("password123"); // Add missing password
        doctor.setFullName("Test Doctor");
        doctor.setSpecialty("General Medicine");
        doctor.setEnabled(true);
        doctor = doctorRepository.save(doctor);

        // Create test slot
        slot = new Slot();
        slot.setDoctor(doctor);
        slot.setStartTime(OffsetDateTime.now().plusDays(1));
        slot.setEndTime(OffsetDateTime.now().plusDays(1).plusHours(1));
        slot.setAvailable(true);
        slot = slotRepository.save(slot);
    }

    @Test
    void happyPath_DoctorCreatesSlots_PatientBooks_DoctorAddsRemarks_PatientViewsRemarks() {
        // 1. Doctor creates slots (already done in setUp)
        assertTrue(slot.isAvailable());
        assertEquals(doctor.getId(), slot.getDoctor().getId());

        // 2. Patient books appointment
        AppointmentDtos.BookRequest bookRequest = new AppointmentDtos.BookRequest(
                patient.getId(), doctor.getId(), slot.getId());

        Long appointmentId = appointmentService.book(bookRequest);
        assertNotNull(appointmentId);

        // Verify slot is no longer available
        Slot updatedSlot = slotRepository.findById(slot.getId()).orElseThrow();
        assertFalse(updatedSlot.isAvailable());

        // 3. Doctor adds remarks and prescription
        String remarks = "Patient shows good progress. Continue current treatment.";
        String prescription = "Continue medication as prescribed. Follow up in 2 weeks.";

        appointmentService.updateRemarks(appointmentId, remarks, prescription);

        // 4. Patient views appointment with remarks
        List<Appointment> patientAppointments = appointmentService.patientAppointments(patient.getId());
        assertEquals(1, patientAppointments.size());

        Appointment appointment = patientAppointments.get(0);
        assertEquals(AppointmentStatus.BOOKED, appointment.getStatus());
        assertEquals(remarks, appointment.getRemarks());
        assertEquals(prescription, appointment.getPrescription());

        // 5. Patient cancels appointment
        appointmentService.cancel(appointmentId);

        // Verify slot is available again
        Slot cancelledSlot = slotRepository.findById(slot.getId()).orElseThrow();
        assertTrue(cancelledSlot.isAvailable());

        // Verify appointment is cancelled
        Appointment cancelledAppointment = appointmentRepository.findById(appointmentId).orElseThrow();
        assertEquals(AppointmentStatus.CANCELLED, cancelledAppointment.getStatus());
    }

    @Test
    void doubleBookingPrevention_ThrowsException() {
        // Book first appointment
        AppointmentDtos.BookRequest bookRequest = new AppointmentDtos.BookRequest(
                patient.getId(), doctor.getId(), slot.getId());
        appointmentService.book(bookRequest);

        // Try to book the same slot again
        Patient anotherPatient = new Patient();
        anotherPatient.setEmail("another@example.com");
        anotherPatient.setPassword("password123"); // Add missing password
        anotherPatient.setFullName("Another Patient");
        anotherPatient.setEnabled(true);
        anotherPatient = patientRepository.save(anotherPatient);

        AppointmentDtos.BookRequest duplicateRequest = new AppointmentDtos.BookRequest(
                anotherPatient.getId(), doctor.getId(), slot.getId());

        // Should throw exception
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> appointmentService.book(duplicateRequest));
        assertTrue(exception.getMessage().contains("no longer available"));
    }
}
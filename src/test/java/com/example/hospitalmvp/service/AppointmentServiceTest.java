package com.example.hospitalmvp.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Patient patient;
    private Doctor doctor;
    private Slot slot;
    private AppointmentDtos.BookRequest bookRequest;

    @BeforeEach
    void setUp() throws Exception {
        patient = new Patient();
        setPrivateField(patient, "id", 1L);
        patient.setEmail("patient@example.com");
        patient.setFullName("John Patient");

        doctor = new Doctor();
        setPrivateField(doctor, "id", 1L);
        doctor.setEmail("doctor@example.com");
        doctor.setFullName("Dr. Smith");

        slot = new Slot();
        setPrivateField(slot, "id", 1L);
        slot.setDoctor(doctor);
        slot.setAvailable(true);
        slot.setStartTime(OffsetDateTime.now().plusDays(1));
        slot.setEndTime(OffsetDateTime.now().plusDays(1).plusHours(1));

        bookRequest = new AppointmentDtos.BookRequest(1L, 1L, 1L);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field;
        // For User subclasses, the id field is in the User class
        if (target instanceof Patient || target instanceof Doctor) {
            field = target.getClass().getSuperclass().getDeclaredField(fieldName);
        } else {
            // For other classes, try the class itself first
            try {
                field = target.getClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                field = target.getClass().getSuperclass().getDeclaredField(fieldName);
            }
        }
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void bookAppointment_Success() throws Exception {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(appointmentRepository.existsBySlotAndStatusIn(any(), any())).thenReturn(false);
        Appointment savedAppointment = new Appointment();
        setPrivateField(savedAppointment, "id", 1L);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

        // When
        Long appointmentId = appointmentService.book(bookRequest);

        // Then
        assertNotNull(appointmentId);
        verify(slotRepository).save(slot);
        verify(appointmentRepository).save(any(Appointment.class));
        assertFalse(slot.isAvailable());
    }

    @Test
    void bookAppointment_SlotNotAvailable_ThrowsException() {
        // Given
        slot.setAvailable(false);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> appointmentService.book(bookRequest));
        assertEquals("This time slot is no longer available", exception.getMessage());
    }

    @Test
    void bookAppointment_DoubleBooking_ThrowsException() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(appointmentRepository.existsBySlotAndStatusIn(any(), any())).thenReturn(true);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> appointmentService.book(bookRequest));
        assertEquals("This slot has already been booked by another patient", exception.getMessage());
    }

    @Test
    void cancelAppointment_Success() throws Exception {
        // Given
        Appointment appointment = new Appointment();
        setPrivateField(appointment, "id", 1L);
        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setSlot(slot);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        // When
        appointmentService.cancel(1L);

        // Then
        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
        assertTrue(slot.isAvailable());
        verify(slotRepository).save(slot);
        verify(appointmentRepository).save(appointment);
    }
}
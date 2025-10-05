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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final SlotRepository slotRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository,
            DoctorRepository doctorRepository, SlotRepository slotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.slotRepository = slotRepository;
    }

    @Transactional
    public Long book(AppointmentDtos.BookRequest req) {
        // Validate patient exists
        Patient patient = patientRepository.findById(req.patientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + req.patientId()));

        // Validate doctor exists
        Doctor doctor = doctorRepository.findById(req.doctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + req.doctorId()));

        // Validate slot exists
        Slot slot = slotRepository.findById(req.slotId())
                .orElseThrow(() -> new IllegalArgumentException("Slot not found with ID: " + req.slotId()));

        // Check if slot is available
        if (!slot.isAvailable()) {
            throw new IllegalStateException("This time slot is no longer available");
        }

        // Verify slot belongs to the specified doctor
        if (!slot.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalArgumentException("This slot does not belong to the specified doctor");
        }

        // Check for double booking
        boolean exists = appointmentRepository.existsBySlotAndStatusIn(slot, List.of(AppointmentStatus.BOOKED));
        if (exists) {
            throw new IllegalStateException("This slot has already been booked by another patient");
        }

        // Book the appointment
        slot.setAvailable(false);
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setSlot(slot);
        appointment.setStatus(AppointmentStatus.BOOKED);

        slotRepository.save(slot);
        return appointmentRepository.save(appointment).getId();
    }

    @Transactional
    public void cancel(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Appointment is already cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Slot slot = appointment.getSlot();
        slot.setAvailable(true);

        slotRepository.save(slot);
        appointmentRepository.save(appointment);
    }

    public List<Appointment> patientAppointments(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientId));
        return appointmentRepository.findByPatient(patient);
    }

    public List<Appointment> doctorAppointments(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + doctorId));
        return appointmentRepository.findByDoctor(doctor);
    }

    @Transactional
    public void updateRemarks(Long appointmentId, String remarks, String prescription) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        appointment.setRemarks(remarks);
        appointment.setPrescription(prescription);
        appointmentRepository.save(appointment);
    }
}
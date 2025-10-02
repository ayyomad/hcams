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
        Patient patient = patientRepository.findById(req.patientId()).orElseThrow();
        Doctor doctor = doctorRepository.findById(req.doctorId()).orElseThrow();
        Slot slot = slotRepository.findById(req.slotId()).orElseThrow();
        if (!slot.isAvailable())
            throw new IllegalStateException("Slot not available");
        if (!slot.getDoctor().getId().equals(doctor.getId()))
            throw new IllegalArgumentException("Slot not for doctor");
        boolean exists = appointmentRepository.existsBySlotAndStatusIn(slot, List.of(AppointmentStatus.BOOKED));
        if (exists)
            throw new IllegalStateException("Slot already booked");
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
        Appointment a = appointmentRepository.findById(appointmentId).orElseThrow();
        if (a.getStatus() == AppointmentStatus.CANCELLED)
            return;
        a.setStatus(AppointmentStatus.CANCELLED);
        Slot slot = a.getSlot();
        slot.setAvailable(true);
        slotRepository.save(slot);
        appointmentRepository.save(a);
    }

    public List<Appointment> patientAppointments(Long patientId) {
        Patient p = patientRepository.findById(patientId).orElseThrow();
        return appointmentRepository.findByPatient(p);
    }

    public List<Appointment> doctorAppointments(Long doctorId) {
        Doctor d = doctorRepository.findById(doctorId).orElseThrow();
        return appointmentRepository.findByDoctor(d);
    }

    @Transactional
    public void updateRemarks(Long appointmentId, String remarks, String prescription) {
        Appointment a = appointmentRepository.findById(appointmentId).orElseThrow();
        a.setRemarks(remarks);
        a.setPrescription(prescription);
        appointmentRepository.save(a);
    }
}

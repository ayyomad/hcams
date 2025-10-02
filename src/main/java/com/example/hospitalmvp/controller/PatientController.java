package com.example.hospitalmvp.controller;

import com.example.hospitalmvp.dto.AppointmentDtos;
import com.example.hospitalmvp.entity.Appointment;
import com.example.hospitalmvp.entity.Slot;
import com.example.hospitalmvp.service.AppointmentService;
import com.example.hospitalmvp.service.SlotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {

    private final AppointmentService appointmentService;
    private final SlotService slotService;

    public PatientController(AppointmentService appointmentService, SlotService slotService) {
        this.appointmentService = appointmentService;
        this.slotService = slotService;
    }

    @PostMapping("/appointments")
    public ResponseEntity<Long> book(@RequestBody @Valid AppointmentDtos.BookRequest req) {
        return ResponseEntity.ok(appointmentService.book(req));
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        appointmentService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/appointments/{patientId}")
    public ResponseEntity<List<Appointment>> myAppointments(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.patientAppointments(patientId));
    }
}

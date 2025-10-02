package com.example.hospitalmvp.controller;

import com.example.hospitalmvp.dto.AppointmentDtos;
import com.example.hospitalmvp.dto.SlotDtos;
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
@RequestMapping("/api/doctor")
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorController {

    private final SlotService slotService;
    private final AppointmentService appointmentService;

    public DoctorController(SlotService slotService, AppointmentService appointmentService) {
        this.slotService = slotService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/slots")
    public ResponseEntity<Long> createSlot(@RequestBody @Valid SlotDtos.CreateSlot req) {
        return ResponseEntity.ok(slotService.createSlot(req));
    }

    @PatchMapping("/slots/{slotId}/availability")
    public ResponseEntity<Void> setAvailability(@PathVariable Long slotId, @RequestParam boolean available) {
        slotService.setAvailability(slotId, available);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/slots/{doctorId}")
    public ResponseEntity<List<Slot>> mySlots(@PathVariable Long doctorId) {
        return ResponseEntity.ok(slotService.listDoctorSlots(doctorId));
    }

    @GetMapping("/appointments/{doctorId}")
    public ResponseEntity<List<Appointment>> myAppointments(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.doctorAppointments(doctorId));
    }

    @PatchMapping("/appointments/{appointmentId}/remarks")
    public ResponseEntity<Void> updateRemarks(@PathVariable Long appointmentId,
            @RequestBody @Valid AppointmentDtos.UpdateRemarks req) {
        appointmentService.updateRemarks(appointmentId, req.remarks(), req.prescription());
        return ResponseEntity.noContent().build();
    }
}

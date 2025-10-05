package com.example.hospitalmvp.controller;

import com.example.hospitalmvp.entity.Slot;
import com.example.hospitalmvp.entity.user.Doctor;
import com.example.hospitalmvp.repository.DoctorRepository;
import com.example.hospitalmvp.repository.SlotRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final DoctorRepository doctorRepository;
    private final SlotRepository slotRepository;

    public PublicController(DoctorRepository doctorRepository, SlotRepository slotRepository) {
        this.doctorRepository = doctorRepository;
        this.slotRepository = slotRepository;
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> listDoctors() {
        return ResponseEntity.ok(doctorRepository.findAll());
    }

    @GetMapping("/doctors/{doctorId}/slots")
    public ResponseEntity<List<Slot>> listAvailableSlots(@PathVariable Long doctorId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String date) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();

        OffsetDateTime start, end;

        if (date != null) {
            // Filter by specific date
            start = OffsetDateTime.parse(date + "T00:00:00Z");
            end = start.plusDays(1);
        } else {
            // Use from/to parameters or default to next 7 days
            start = from != null ? OffsetDateTime.parse(from) : OffsetDateTime.now();
            end = to != null ? OffsetDateTime.parse(to) : start.plusDays(7);
        }

        // Return ALL slots (both available and booked) for better UI display
        return ResponseEntity.ok(slotRepository.findByDoctorAndStartTimeBetween(doctor, start, end));
    }
}

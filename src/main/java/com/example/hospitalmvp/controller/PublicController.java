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
            @RequestParam(required = false) String from, @RequestParam(required = false) String to) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        OffsetDateTime start = from != null ? OffsetDateTime.parse(from) : OffsetDateTime.now();
        OffsetDateTime end = to != null ? OffsetDateTime.parse(to) : start.plusDays(7);
        return ResponseEntity.ok(slotRepository.findByDoctorAndStartTimeBetweenAndAvailableIsTrue(doctor, start, end));
    }
}

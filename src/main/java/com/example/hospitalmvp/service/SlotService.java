package com.example.hospitalmvp.service;

import com.example.hospitalmvp.dto.SlotDtos;
import com.example.hospitalmvp.entity.Slot;
import com.example.hospitalmvp.entity.user.Doctor;
import com.example.hospitalmvp.repository.DoctorRepository;
import com.example.hospitalmvp.repository.SlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SlotService {
    private final SlotRepository slotRepository;
    private final DoctorRepository doctorRepository;

    public SlotService(SlotRepository slotRepository, DoctorRepository doctorRepository) {
        this.slotRepository = slotRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public Long createSlot(SlotDtos.CreateSlot req) {
        Doctor doctor = doctorRepository.findById(req.doctorId()).orElseThrow();
        if (!req.endTime().isAfter(req.startTime()))
            throw new IllegalArgumentException("End must be after start");
        slotRepository.findByDoctorAndStartTime(doctor, req.startTime()).ifPresent(s -> {
            throw new IllegalArgumentException("Slot already exists");
        });
        Slot slot = new Slot();
        slot.setDoctor(doctor);
        slot.setStartTime(req.startTime());
        slot.setEndTime(req.endTime());
        slot.setAvailable(true);
        return slotRepository.save(slot).getId();
    }

    public List<Slot> listDoctorSlots(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        return slotRepository.findByDoctor(doctor);
    }

    @Transactional
    public void setAvailability(Long slotId, boolean available) {
        Slot slot = slotRepository.findById(slotId).orElseThrow();
        slot.setAvailable(available);
        slotRepository.save(slot);
    }
}

package com.example.hospitalmvp.repository;

import com.example.hospitalmvp.entity.Slot;
import com.example.hospitalmvp.entity.user.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByDoctorAndStartTimeBetweenAndAvailableIsTrue(Doctor doctor, OffsetDateTime start,
            OffsetDateTime end);

    List<Slot> findByDoctorAndStartTimeBetween(Doctor doctor, OffsetDateTime start, OffsetDateTime end);

    List<Slot> findByDoctor(Doctor doctor);

    Optional<Slot> findByDoctorAndStartTime(Doctor doctor, OffsetDateTime startTime);
}

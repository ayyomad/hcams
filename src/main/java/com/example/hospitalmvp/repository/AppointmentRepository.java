package com.example.hospitalmvp.repository;

import com.example.hospitalmvp.entity.Appointment;
import com.example.hospitalmvp.entity.AppointmentStatus;
import com.example.hospitalmvp.entity.Slot;
import com.example.hospitalmvp.entity.user.Doctor;
import com.example.hospitalmvp.entity.user.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.OffsetDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsBySlotAndStatusIn(Slot slot, List<AppointmentStatus> statuses);

    List<Appointment> findByPatient(Patient patient);

    List<Appointment> findByDoctor(Doctor doctor);

    long countBySlot_StartTimeBetweenAndStatusNot(OffsetDateTime startInclusive, OffsetDateTime endExclusive,
            AppointmentStatus status);
}

package com.example.hospitalmvp.repository;

import com.example.hospitalmvp.entity.user.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}

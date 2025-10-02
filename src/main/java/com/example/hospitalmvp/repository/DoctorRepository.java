package com.example.hospitalmvp.repository;

import com.example.hospitalmvp.entity.user.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}

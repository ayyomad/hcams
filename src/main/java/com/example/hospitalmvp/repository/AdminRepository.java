package com.example.hospitalmvp.repository;

import com.example.hospitalmvp.entity.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}

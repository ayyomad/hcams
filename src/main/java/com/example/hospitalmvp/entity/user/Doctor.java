package com.example.hospitalmvp.entity.user;

import com.example.hospitalmvp.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctors")
public class Doctor extends User {

    @Column(length = 120)
    private String specialty;

    public Doctor() {
        setRole(Role.DOCTOR);
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}

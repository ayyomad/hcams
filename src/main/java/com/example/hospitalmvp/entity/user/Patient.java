package com.example.hospitalmvp.entity.user;

import com.example.hospitalmvp.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "patients")
public class Patient extends User {

    @Column(length = 20)
    private String phone;

    public Patient() {
        setRole(Role.PATIENT);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

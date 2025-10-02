package com.example.hospitalmvp.entity.user;

import com.example.hospitalmvp.entity.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User {
    public Admin() {
        setRole(Role.ADMIN);
    }
}

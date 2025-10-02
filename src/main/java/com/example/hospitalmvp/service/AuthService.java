package com.example.hospitalmvp.service;

import com.example.hospitalmvp.dto.AuthDtos;
import com.example.hospitalmvp.entity.Role;
import com.example.hospitalmvp.entity.user.Doctor;
import com.example.hospitalmvp.entity.user.Patient;
import com.example.hospitalmvp.entity.user.User;
import com.example.hospitalmvp.repository.DoctorRepository;
import com.example.hospitalmvp.repository.PatientRepository;
import com.example.hospitalmvp.repository.UserRepository;
import com.example.hospitalmvp.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AuthService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
            JwtService jwtService, UserRepository userRepository, PatientRepository patientRepository,
            DoctorRepository doctorRepository) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public String login(AuthDtos.LoginRequest req) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) auth
                .getPrincipal();
        User user = userRepository.findByEmail(principal.getUsername()).orElseThrow();
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("userId", user.getId());
        return jwtService.generateToken(user.getEmail(), claims);
    }

    public Long registerPatient(AuthDtos.RegisterPatient req) {
        if (userRepository.existsByEmail(req.email()))
            throw new IllegalArgumentException("Email already exists");
        Patient p = new Patient();
        p.setEmail(req.email());
        p.setPassword(passwordEncoder.encode(req.password()));
        p.setFullName(req.fullName());
        p.setEnabled(true);
        p.setRole(Role.PATIENT);
        p.setPhone(req.phone());
        return patientRepository.save(p).getId();
    }

    public Long registerDoctor(AuthDtos.RegisterDoctor req) {
        if (userRepository.existsByEmail(req.email()))
            throw new IllegalArgumentException("Email already exists");
        Doctor d = new Doctor();
        d.setEmail(req.email());
        d.setPassword(passwordEncoder.encode(req.password()));
        d.setFullName(req.fullName());
        d.setEnabled(true);
        d.setRole(Role.DOCTOR);
        d.setSpecialty(req.specialty());
        return doctorRepository.save(d).getId();
    }
}

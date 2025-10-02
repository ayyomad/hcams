package com.example.hospitalmvp.controller;

import com.example.hospitalmvp.dto.AuthDtos;
import com.example.hospitalmvp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.TokenResponse> login(@RequestBody @Valid AuthDtos.LoginRequest req) {
        String token = authService.login(req);
        return ResponseEntity.ok(new AuthDtos.TokenResponse(token));
    }

    @PostMapping("/register/patient")
    public ResponseEntity<Long> registerPatient(@RequestBody @Valid AuthDtos.RegisterPatient req) {
        return ResponseEntity.ok(authService.registerPatient(req));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<Long> registerDoctor(@RequestBody @Valid AuthDtos.RegisterDoctor req) {
        return ResponseEntity.ok(authService.registerDoctor(req));
    }
}

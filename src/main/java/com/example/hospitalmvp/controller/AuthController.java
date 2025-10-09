package com.example.hospitalmvp.controller;

import com.example.hospitalmvp.dto.AuthDtos;
import com.example.hospitalmvp.service.AuthService;
import com.example.hospitalmvp.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;

    public AuthController(AuthService authService, OtpService otpService) {
        this.authService = authService;
        this.otpService = otpService;
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
    
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        otpService.generateAndSendOtp(email);
        return ResponseEntity.ok(Map.of("message", "OTP sent to " + email));
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Boolean>> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        boolean valid = otpService.verifyOtp(email, otp);
        return ResponseEntity.ok(Map.of("valid", valid));
    }
}

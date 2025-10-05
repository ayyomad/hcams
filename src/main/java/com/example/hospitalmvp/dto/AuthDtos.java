package com.example.hospitalmvp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
        public record LoginRequest(@Email @NotBlank String email, @NotBlank String password, boolean rememberMe) {
        }

        public record TokenResponse(String token) {
        }

        public record RegisterPatient(@Email @NotBlank String email, @NotBlank @Size(min = 6) String password,
                        @NotBlank String fullName, String phone) {
        }

        public record RegisterDoctor(@Email @NotBlank String email, @NotBlank @Size(min = 6) String password,
                        @NotBlank String fullName, String specialty) {
        }
}

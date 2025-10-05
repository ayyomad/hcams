package com.example.hospitalmvp.controller;

import com.example.hospitalmvp.entity.user.User;
import com.example.hospitalmvp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TestController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<Map<String, Object>> getUserInfo(@PathVariable String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "User not found");
            response.put("email", email);
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("fullName", user.getFullName());
        response.put("role", user.getRole());
        response.put("enabled", user.isEnabled());
        response.put("passwordHash", user.getPassword());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-password")
    public ResponseEntity<Map<String, Object>> verifyPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);

        Map<String, Object> response = new HashMap<>();

        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("error", "User not found");
            return ResponseEntity.ok(response);
        }

        User user = userOpt.get();
        boolean matches = passwordEncoder.matches(password, user.getPassword());

        response.put("success", matches);
        response.put("email", email);
        response.put("userExists", true);
        response.put("passwordMatches", matches);
        response.put("userEnabled", user.isEnabled());
        response.put("userRole", user.getRole());

        if (!matches) {
            response.put("error", "Password does not match");
            response.put("storedHash", user.getPassword());
            response.put("providedPassword", password);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalUsers", userRepository.count());
        response.put("users", userRepository.findAll().stream()
                .map(user -> Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "fullName", user.getFullName(),
                        "role", user.getRole(),
                        "enabled", user.isEnabled()))
                .toList());

        return ResponseEntity.ok(response);
    }
}

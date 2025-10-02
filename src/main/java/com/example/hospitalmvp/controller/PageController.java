package com.example.hospitalmvp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/doctor-login")
    public String doctorLogin() {
        return "doctor-login";
    }

    @GetMapping("/admin-login")
    public String adminLogin() {
        return "admin-login";
    }

    @GetMapping("/patient/dashboard")
    public String patientDashboard() {
        return "patient-dashboard";
    }

    @GetMapping("/doctor/dashboard")
    public String doctorDashboard() {
        return "doctor-dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }
}

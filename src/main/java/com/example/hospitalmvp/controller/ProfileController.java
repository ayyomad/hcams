package com.example.hospitalmvp.controller;

import com.example.hospitalmvp.entity.Appointment;
import com.example.hospitalmvp.entity.AppointmentStatus;
import com.example.hospitalmvp.entity.user.Doctor;
import com.example.hospitalmvp.entity.user.Patient;
import com.example.hospitalmvp.entity.user.User;
import com.example.hospitalmvp.repository.AppointmentRepository;
import com.example.hospitalmvp.repository.DoctorRepository;
import com.example.hospitalmvp.repository.PatientRepository;
import com.example.hospitalmvp.repository.UserRepository;
import com.example.hospitalmvp.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public ProfileController(JwtService jwtService, UserRepository userRepository,
            PatientRepository patientRepository, DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping
    public String profile(HttpServletRequest request, Model model,
            @RequestParam(defaultValue = "upcoming") String tab) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            return "redirect:/login";
        }

        try {
            Map<String, Object> claims = jwtService.extractAllClaims(token);
            String email = claims.get("sub").toString();
            User user = userRepository.findByEmail(email).orElseThrow();

            model.addAttribute("user", user);
            model.addAttribute("activeTab", tab);

            if (user.getRole().name().equals("PATIENT")) {
                return handlePatientProfile(user, model, tab);
            } else if (user.getRole().name().equals("DOCTOR")) {
                return handleDoctorProfile(user, model, tab);
            } else if (user.getRole().name().equals("ADMIN")) {
                return handleAdminProfile(user, model, tab);
            }

        } catch (Exception e) {
            return "redirect:/login";
        }

        return "redirect:/login";
    }

    private String handlePatientProfile(User user, Model model, String tab) {
        Patient patient = patientRepository.findById(user.getId()).orElseThrow();
        model.addAttribute("patient", patient);

        LocalDate today = LocalDate.now();
        OffsetDateTime startOfDay = today.atStartOfDay().atOffset(java.time.ZoneOffset.UTC);
        OffsetDateTime endOfDay = today.plusDays(1).atStartOfDay().atOffset(java.time.ZoneOffset.UTC);

        List<Appointment> allAppointments = appointmentRepository.findByPatient(patient);

        List<Appointment> upcomingAppointments = allAppointments.stream()
                .filter(apt -> apt.getStatus() == AppointmentStatus.BOOKED)
                .filter(apt -> apt.getSlot().getStartTime().isAfter(OffsetDateTime.now()))
                .sorted((a, b) -> a.getSlot().getStartTime().compareTo(b.getSlot().getStartTime()))
                .toList();

        List<Appointment> pastAppointments = allAppointments.stream()
                .filter(apt -> apt.getStatus() == AppointmentStatus.COMPLETED ||
                        apt.getSlot().getStartTime().isBefore(OffsetDateTime.now()))
                .sorted((a, b) -> b.getSlot().getStartTime().compareTo(a.getSlot().getStartTime()))
                .toList();

        List<Appointment> reports = allAppointments.stream()
                .filter(apt -> apt.getStatus() == AppointmentStatus.COMPLETED)
                .filter(apt -> apt.getPrescription() != null && !apt.getPrescription().isEmpty())
                .sorted((a, b) -> b.getSlot().getStartTime().compareTo(a.getSlot().getStartTime()))
                .toList();

        model.addAttribute("upcomingAppointments", upcomingAppointments);
        model.addAttribute("pastAppointments", pastAppointments);
        model.addAttribute("reports", reports);

        return "patient-profile";
    }

    private String handleDoctorProfile(User user, Model model, String tab) {
        Doctor doctor = doctorRepository.findById(user.getId()).orElseThrow();
        model.addAttribute("doctor", doctor);

        LocalDate today = LocalDate.now();
        OffsetDateTime startOfDay = today.atStartOfDay().atOffset(java.time.ZoneOffset.UTC);
        OffsetDateTime endOfDay = today.plusDays(1).atStartOfDay().atOffset(java.time.ZoneOffset.UTC);

        List<Appointment> allAppointments = appointmentRepository.findByDoctor(doctor);

        List<Appointment> todayAppointments = allAppointments.stream()
                .filter(apt -> apt.getSlot().getStartTime().isAfter(startOfDay) &&
                        apt.getSlot().getStartTime().isBefore(endOfDay))
                .filter(apt -> apt.getStatus() == AppointmentStatus.BOOKED)
                .sorted((a, b) -> a.getSlot().getStartTime().compareTo(b.getSlot().getStartTime()))
                .toList();

        List<Appointment> upcomingAppointments = allAppointments.stream()
                .filter(apt -> apt.getStatus() == AppointmentStatus.BOOKED)
                .filter(apt -> apt.getSlot().getStartTime().isAfter(OffsetDateTime.now()))
                .sorted((a, b) -> a.getSlot().getStartTime().compareTo(b.getSlot().getStartTime()))
                .toList();

        List<Appointment> completedAppointments = allAppointments.stream()
                .filter(apt -> apt.getStatus() == AppointmentStatus.COMPLETED)
                .sorted((a, b) -> b.getSlot().getStartTime().compareTo(a.getSlot().getStartTime()))
                .toList();

        model.addAttribute("todayAppointments", todayAppointments);
        model.addAttribute("upcomingAppointments", upcomingAppointments);
        model.addAttribute("completedAppointments", completedAppointments);
        model.addAttribute("totalAppointments", allAppointments.size());
        model.addAttribute("todayCount", todayAppointments.size());
        model.addAttribute("upcomingCount", upcomingAppointments.size());
        model.addAttribute("completedCount", completedAppointments.size());

        return "doctor-profile";
    }

    private String handleAdminProfile(User user, Model model, String tab) {
        model.addAttribute("admin", user);
        return "admin-profile";
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // Try to get from cookie
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("authToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}

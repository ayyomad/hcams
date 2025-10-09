package com.example.hospitalmvp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    public void sendOtp(String to, String otp) {
        if (mailSender == null) {
            System.out.println("========================================");
            System.out.println("📧 EMAIL NOT CONFIGURED");
            System.out.println("OTP for " + to + ": " + otp);
            System.out.println("Configure email in application.properties to send real emails");
            System.out.println("========================================");
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@hcams.com");
            message.setTo(to);
            message.setSubject("HCAMS - Email Verification OTP");
            message.setText(
                "Your OTP for HCAMS registration is: " + otp + "\n\n" +
                "This OTP is valid for 10 minutes.\n\n" +
                "If you didn't request this, please ignore this email.\n\n" +
                "- HCAMS Team"
            );
            
            mailSender.send(message);
            System.out.println("✅ OTP sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            System.out.println("OTP for " + to + ": " + otp + " (fallback - email failed)");
        }
    }
}

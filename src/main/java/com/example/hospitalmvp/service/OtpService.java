package com.example.hospitalmvp.service;

import com.example.hospitalmvp.entity.OtpVerification;
import com.example.hospitalmvp.repository.OtpVerificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {
    
    private final OtpVerificationRepository otpRepository;
    private final EmailService emailService;
    private static final SecureRandom random = new SecureRandom();
    
    public OtpService(OtpVerificationRepository otpRepository, EmailService emailService) {
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }
    
    @Transactional
    public void generateAndSendOtp(String email) {
        // Delete any previous unverified OTPs for this email
        otpRepository.deleteByEmailAndVerifiedFalse(email);
        
        // Generate 6-digit OTP
        String otp = String.format("%06d", random.nextInt(1000000));
        
        // Set expiry to 10 minutes from now
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);
        
        // Save OTP
        OtpVerification verification = new OtpVerification(email, otp, expiryTime);
        otpRepository.save(verification);
        
        // Send email
        emailService.sendOtp(email, otp);
    }
    
    public boolean verifyOtp(String email, String otp) {
        LocalDateTime now = LocalDateTime.now();
        
        Optional<OtpVerification> optVerification = otpRepository
            .findByEmailAndOtpAndVerifiedFalseAndExpiryTimeAfter(email, otp, now);
        
        if (optVerification.isPresent()) {
            OtpVerification verification = optVerification.get();
            verification.setVerified(true);
            otpRepository.save(verification);
            return true;
        }
        
        return false;
    }
}

package com.example.hospitalmvp.repository;

import com.example.hospitalmvp.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findByEmailAndOtpAndVerifiedFalseAndExpiryTimeAfter(
        String email, String otp, LocalDateTime currentTime);
    
    Optional<OtpVerification> findTopByEmailOrderByCreatedAtDesc(String email);
    
    @Modifying
    @Transactional
    void deleteByEmailAndVerifiedFalse(String email);
}

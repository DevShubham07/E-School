package com.school.erp.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school.erp.domain.entity.OtpRequest;
import com.school.erp.repository.OtpRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OtpService {

    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 10;
    private static final int MAX_ATTEMPTS = 5;

    private final OtpRequestRepository otpRequestRepository;
    private final SmsService smsService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final SecureRandom random = new SecureRandom();

    public String generateAndSendOtp(String phoneNumber) {
        // Invalidate previous unused OTPs for this phone number
        otpRequestRepository.markAllAsUsedByPhoneNumber(phoneNumber);

        // Generate numeric OTP
        String otp = generateNumericOtp();

        // Hash the OTP
        String otpHash = passwordEncoder.encode(otp);

        // Create OTP request
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setPhoneNumber(phoneNumber);
        otpRequest.setOtpHash(otpHash);
        otpRequest.setExpiresAt(Instant.now().plusSeconds(OTP_VALIDITY_MINUTES * 60L));
        otpRequest.setAttemptCount(0);
        otpRequest.setIsUsed(false);

        otpRequestRepository.save(otpRequest);

        // Send OTP via SMS (mock)
        smsService.sendOtp(phoneNumber, otp);

        return otp;
    }

    public boolean verifyOtp(String phoneNumber, String otp) {
        Optional<OtpRequest> otpRequestOpt = otpRequestRepository
            .findFirstByPhoneNumberAndIsUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
                phoneNumber, Instant.now());

        if (otpRequestOpt.isEmpty()) {
            return false;
        }

        OtpRequest otpRequest = otpRequestOpt.get();

        // Check attempt count
        if (otpRequest.getAttemptCount() >= MAX_ATTEMPTS) {
            otpRequest.setIsUsed(true);
            otpRequestRepository.save(otpRequest);
            return false;
        }

        // Verify OTP
        boolean isValid = passwordEncoder.matches(otp, otpRequest.getOtpHash());

        // Increment attempt count
        otpRequest.setAttemptCount(otpRequest.getAttemptCount() + 1);

        if (isValid) {
            otpRequest.setIsUsed(true);
        }

        otpRequestRepository.save(otpRequest);

        return isValid;
    }

    private String generateNumericOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}

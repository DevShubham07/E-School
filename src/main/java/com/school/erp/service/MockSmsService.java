package com.school.erp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockSmsService implements SmsService {

    @Override
    public void sendOtp(String phoneNumber, String otp) {
        log.info("=== MOCK SMS SERVICE ===");
        log.info("To: {}", phoneNumber);
        log.info("OTP: {}", otp);
        log.info("========================");
        // In production, this would integrate with an actual SMS gateway
    }
}

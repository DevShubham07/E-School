package com.school.erp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtp(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply.eschool365@gmail.com");
            message.setTo(email);
            message.setSubject("E-School OTP Verification");
            message.setText("Your OTP for E-School login is: " + otp + "\n\nThis OTP is valid for 15 minutes.\n\nDo not share this OTP with anyone.");
            
            mailSender.send(message);
            log.info("OTP email sent successfully to: {} with OTP: {}", email, otp);
        } catch (Exception e) {
            log.error("Failed to send OTP email to: {}", email, e);
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
}

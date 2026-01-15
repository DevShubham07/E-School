package com.school.erp.service;

public interface SmsService {
    void sendOtp(String phoneNumber, String otp);
}

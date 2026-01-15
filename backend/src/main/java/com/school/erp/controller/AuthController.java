package com.school.erp.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.erp.domain.entity.Student;
import com.school.erp.domain.entity.Teacher;
import com.school.erp.dto.AuthResponseDto;
import com.school.erp.dto.OtpRequestDto;
import com.school.erp.dto.OtpVerifyDto;
import com.school.erp.repository.StudentRepository;
import com.school.erp.repository.TeacherRepository;
import com.school.erp.security.JwtUtil;
import com.school.erp.service.OtpService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OtpService otpService;
    private final JwtUtil jwtUtil;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @PostMapping("/request-otp")
    public ResponseEntity<Map<String, String>> requestOtp(@Valid @RequestBody OtpRequestDto request) {
        otpService.generateAndSendOtp(request.getPhone());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP sent successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpVerifyDto request) {
        boolean isValid = otpService.verifyOtp(request.getPhone(), request.getOtp());

        if (!isValid) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid or expired OTP");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        // Resolve user (Teacher or Student)
        Optional<Teacher> teacherOpt = teacherRepository.findByPhone(request.getPhone());
        Optional<Student> studentOpt = studentRepository.findByPhone(request.getPhone());

        Long userId;
        String role;

        if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            if (!teacher.getIsActive()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Teacher account is inactive");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            userId = teacher.getId();
            role = "ROLE_TEACHER";
        } else if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (!student.getIsActive()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Student account is inactive");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            userId = student.getId();
            role = "ROLE_STUDENT";
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found with phone number: " + request.getPhone());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(userId, role, request.getPhone());

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(token);
        response.setUserId(userId);
        response.setRole(role);
        response.setPhoneNumber(request.getPhone());

        return ResponseEntity.ok(response);
    }
}

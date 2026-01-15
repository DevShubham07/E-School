package com.school.erp.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        // Validate that either phone or email is provided
        if ((request.getPhone() == null || request.getPhone().trim().isEmpty()) &&
            (request.getEmail() == null || request.getEmail().trim().isEmpty())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Either phone number or email is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            // Send OTP via phone
            otpService.generateAndSendOtp(request.getPhone());
        } else if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            // Send OTP via email
            otpService.generateAndSendOtpByEmail(request.getEmail());
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP sent successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpVerifyDto request) {
        // Validate that either phone or email is provided
        if ((request.getPhone() == null || request.getPhone().trim().isEmpty()) &&
            (request.getEmail() == null || request.getEmail().trim().isEmpty())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Either phone number or email is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        boolean isValid;
        String identifier;
        String identifierType;

        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            isValid = otpService.verifyOtp(request.getPhone(), request.getOtp());
            identifier = request.getPhone();
            identifierType = "phone";
        } else {
            isValid = otpService.verifyOtpByEmail(request.getEmail(), request.getOtp());
            identifier = request.getEmail();
            identifierType = "email";
        }

        if (!isValid) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid or expired OTP");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        // Resolve user (Teacher or Student)
        Optional<Teacher> teacherOpt;
        Optional<Student> studentOpt;

        if ("phone".equals(identifierType)) {
            teacherOpt = teacherRepository.findByPhone(identifier);
            studentOpt = studentRepository.findByPhone(identifier);
        } else {
            teacherOpt = teacherRepository.findByEmail(identifier);
            studentOpt = studentRepository.findByEmail(identifier);
        }

        Long userId;
        String role;
        String phoneNumber = null;
        String email = null;

        if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            if (!teacher.getIsActive()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Teacher account is inactive");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            userId = teacher.getId();
            role = "ROLE_TEACHER";
            phoneNumber = teacher.getPhone();
            email = teacher.getEmail();
        } else if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (!student.getIsActive()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Student account is inactive");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            userId = student.getId();
            role = "ROLE_STUDENT";
            phoneNumber = student.getPhone();
            email = student.getEmail();
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found with " + identifierType + ": " + identifier);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        // Generate JWT token (use phone if available, otherwise email)
        String tokenIdentifier = phoneNumber != null ? phoneNumber : email;
        String token = jwtUtil.generateToken(userId, role, tokenIdentifier);

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(token);
        response.setUserId(userId);
        response.setRole(role);
        response.setPhoneNumber(phoneNumber);
        if (email != null) {
            response.setEmail(email);
        }

        return ResponseEntity.ok(response);
    }
}

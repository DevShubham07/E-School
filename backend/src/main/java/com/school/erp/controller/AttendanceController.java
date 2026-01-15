package com.school.erp.controller;

import com.school.erp.dto.AttendanceRecordResponseDto;
import com.school.erp.dto.AttendanceSessionResponseDto;
import com.school.erp.dto.CreateAttendanceSessionDto;
import com.school.erp.dto.MarkAttendanceDto;
import com.school.erp.security.UserDetailsImpl;
import com.school.erp.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
@Validated
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/session")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AttendanceSessionResponseDto> createSession(
            @Valid @RequestBody CreateAttendanceSessionDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            AttendanceSessionResponseDto session = attendanceService.createSession(dto, userDetails.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(session);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/mark")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AttendanceRecordResponseDto> markAttendance(
            @Valid @RequestBody MarkAttendanceDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            AttendanceRecordResponseDto record = attendanceService.markAttendance(dto, userDetails.getUserId());
            return ResponseEntity.ok(record);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/session/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AttendanceSessionResponseDto> getSession(@PathVariable Long id) {
        try {
            AttendanceSessionResponseDto session = attendanceService.getSessionById(id);
            return ResponseEntity.ok(session);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<List<AttendanceRecordResponseDto>> getMyAttendance(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // For students, only return their own records
        // For teachers, they can view any student's records (can be extended later)
        List<AttendanceRecordResponseDto> records = attendanceService.getMyAttendance(userDetails.getUserId());
        return ResponseEntity.ok(records);
    }

    @GetMapping("/session/{id}/records")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<AttendanceRecordResponseDto>> getSessionRecords(@PathVariable Long id) {
        try {
            List<AttendanceRecordResponseDto> records = attendanceService.getSessionRecords(id);
            return ResponseEntity.ok(records);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

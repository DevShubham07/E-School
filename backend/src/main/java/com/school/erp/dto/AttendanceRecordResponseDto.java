package com.school.erp.dto;

import com.school.erp.domain.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordResponseDto {
    private Long id;
    private Long attendanceSessionId;
    private LocalDate attendanceDate;
    private Long studentId;
    private String studentName;
    private String admissionNumber;
    private AttendanceStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}

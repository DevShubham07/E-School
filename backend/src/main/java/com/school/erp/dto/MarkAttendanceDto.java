package com.school.erp.dto;

import com.school.erp.domain.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarkAttendanceDto {

    @NotNull(message = "Attendance session ID is required")
    private Long attendanceSessionId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Status is required")
    private AttendanceStatus status;
}

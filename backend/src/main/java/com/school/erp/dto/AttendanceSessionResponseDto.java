package com.school.erp.dto;

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
public class AttendanceSessionResponseDto {
    private Long id;
    private LocalDate attendanceDate;
    private Long classSectionId;
    private String className;
    private String sectionName;
    private Long markedByTeacherId;
    private String teacherName;
    private Instant createdAt;
    private Instant updatedAt;
}

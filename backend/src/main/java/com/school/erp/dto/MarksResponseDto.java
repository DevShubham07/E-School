package com.school.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarksResponseDto {
    private Long id;
    private Long examId;
    private String examName;
    private String subject;
    private LocalDate examDate;
    private Long studentId;
    private String studentName;
    private String admissionNumber;
    private BigDecimal marksObtained;
    private BigDecimal maxMarks;
    private BigDecimal percentage;
    private Long gradedByTeacherId;
    private String teacherName;
    private Instant createdAt;
    private Instant updatedAt;
}

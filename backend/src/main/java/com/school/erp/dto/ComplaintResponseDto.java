package com.school.erp.dto;

import com.school.erp.domain.enums.ComplaintStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponseDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private String admissionNumber;
    private Long teacherId;
    private String teacherName;
    private String title;
    private String description;
    private ComplaintStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}

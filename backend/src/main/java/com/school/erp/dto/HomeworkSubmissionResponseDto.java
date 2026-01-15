package com.school.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkSubmissionResponseDto {
    private Long id;
    private Long homeworkId;
    private String homeworkTitle;
    private Long studentId;
    private String studentName;
    private String admissionNumber;
    private String submissionText;
    private String fileUrl;
    private Instant submittedAt;
    private Instant createdAt;
    private Instant updatedAt;
}

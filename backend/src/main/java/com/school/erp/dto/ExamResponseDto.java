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
public class ExamResponseDto {
    private Long id;
    private String name;
    private String subject;
    private LocalDate examDate;
    private Long classSectionId;
    private String className;
    private String sectionName;
    private Long createdByTeacherId;
    private String teacherName;
    private Instant createdAt;
    private Instant updatedAt;
}

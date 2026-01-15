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
public class HomeworkResponseDto {
    private Long id;
    private String title;
    private String description;
    private String subject;
    private LocalDate dueDate;
    private Long classSectionId;
    private String className;
    private String sectionName;
    private Long assignedByTeacherId;
    private String teacherName;
    private Instant createdAt;
    private Instant updatedAt;
}

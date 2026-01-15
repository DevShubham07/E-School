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
public class StudyMaterialResponseDto {
    private Long id;
    private String title;
    private String description;
    private String subject;
    private Long classSectionId;
    private String className;
    private String sectionName;
    private String fileUrl;
    private Long uploadedByTeacherId;
    private String teacherName;
    private Instant createdAt;
    private Instant updatedAt;
}

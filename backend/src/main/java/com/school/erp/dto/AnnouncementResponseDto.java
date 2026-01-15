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
public class AnnouncementResponseDto {
    private Long id;
    private String title;
    private String message;
    private Long classSectionId;
    private String className;
    private String sectionName;
    private Long createdByTeacherId;
    private String teacherName;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant updatedAt;
}

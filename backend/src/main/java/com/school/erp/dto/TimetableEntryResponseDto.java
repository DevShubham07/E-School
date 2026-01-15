package com.school.erp.dto;

import com.school.erp.domain.enums.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimetableEntryResponseDto {
    private Long id;
    private Long classSectionId;
    private String className;
    private String sectionName;
    private DayOfWeek dayOfWeek;
    private Integer periodNumber;
    private String subject;
    private Long teacherId;
    private String teacherName;
    private Instant createdAt;
    private Instant updatedAt;
}

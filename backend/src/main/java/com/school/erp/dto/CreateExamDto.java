package com.school.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateExamDto {

    @NotBlank(message = "Exam name is required")
    @Size(min = 1, max = 200, message = "Exam name must be between 1 and 200 characters")
    private String name;

    @NotBlank(message = "Subject is required")
    @Size(min = 1, max = 100, message = "Subject must be between 1 and 100 characters")
    private String subject;

    @NotNull(message = "Exam date is required")
    private LocalDate examDate;

    @NotNull(message = "Class section ID is required")
    private Long classSectionId;
}

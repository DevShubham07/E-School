package com.school.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClassSectionDto {

    @NotBlank(message = "Class name is required")
    @Size(min = 1, max = 50, message = "Class name must be between 1 and 50 characters")
    private String className;

    @NotBlank(message = "Section name is required")
    @Size(min = 1, max = 10, message = "Section name must be between 1 and 10 characters")
    private String sectionName;
}

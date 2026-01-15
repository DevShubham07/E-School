package com.school.erp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitHomeworkDto {

    @NotNull(message = "Homework ID is required")
    private Long homeworkId;

    @Size(max = 5000, message = "Submission text must not exceed 5000 characters")
    private String submissionText;

    @Size(max = 500, message = "File URL must not exceed 500 characters")
    private String fileUrl;
}

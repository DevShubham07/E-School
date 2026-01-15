package com.school.erp.dto;

import com.school.erp.domain.enums.DayOfWeek;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTimetableEntryDto {

    private DayOfWeek dayOfWeek;

    @Min(value = 1, message = "Period number must be at least 1")
    @Max(value = 10, message = "Period number must be at most 10")
    private Integer periodNumber;

    @Size(min = 1, max = 100, message = "Subject must be between 1 and 100 characters")
    private String subject;

    private Long teacherId;
}

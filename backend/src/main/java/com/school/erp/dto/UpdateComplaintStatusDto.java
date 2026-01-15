package com.school.erp.dto;

import com.school.erp.domain.enums.ComplaintStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateComplaintStatusDto {

    @NotNull(message = "Status is required")
    private ComplaintStatus status;
}

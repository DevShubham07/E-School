package com.school.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDto {
    private String url;
    private String name;
    private String type; // MIME type (e.g., "application/pdf", "image/png")
    private Long size; // File size in bytes (optional)
}

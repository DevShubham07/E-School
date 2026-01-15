package com.school.erp.dto;

import java.time.Instant;
import java.util.List;

import com.school.erp.domain.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private Long userId;
    private String userType;
    private NotificationType type;
    private String title;
    private String message;
    private String data;
    private List<AttachmentDto> attachments;
    private Boolean isRead;
    private Instant readAt;
    private Instant createdAt;
    private Instant updatedAt;
}

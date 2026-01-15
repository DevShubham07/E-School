package com.school.erp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.erp.domain.entity.Notification;
import com.school.erp.domain.enums.NotificationType;
import com.school.erp.dto.AttachmentDto;
import com.school.erp.dto.NotificationResponseDto;
import com.school.erp.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SseNotificationService sseNotificationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Notification createNotification(Long userId, String userType, NotificationType type, 
                                          String title, String message, Map<String, Object> data,
                                          List<AttachmentDto> attachments) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setUserType(userType);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setIsRead(false);

        // Convert data map to JSON string
        if (data != null && !data.isEmpty()) {
            try {
                notification.setData(objectMapper.writeValueAsString(data));
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize notification data", e);
            }
        }

        // Convert attachments list to JSON string
        if (attachments != null && !attachments.isEmpty()) {
            try {
                notification.setAttachments(objectMapper.writeValueAsString(attachments));
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize notification attachments", e);
            }
        }

        notification = notificationRepository.save(notification);
        
        // Send notification via SSE
        sendNotification(notification);

        return notification;
    }

    // Overloaded method without attachments for backward compatibility
    public Notification createNotification(Long userId, String userType, NotificationType type, 
                                          String title, String message, Map<String, Object> data) {
        return createNotification(userId, userType, type, title, message, data, null);
    }

    public void sendNotification(Notification notification) {
        try {
            // Send via SSE for real-time delivery
            sseNotificationService.sendToUser(
                notification.getUserId(), 
                notification.getUserType(), 
                notification
            );
            log.debug("Notification sent via SSE to user {} ({})", 
                notification.getUserId(), notification.getUserType());
        } catch (Exception e) {
            log.error("Failed to send notification via SSE", e);
            // Don't throw - notification is saved, just SSE delivery failed
        }
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponseDto> getUserNotifications(Long userId, String userType, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByUserIdAndUserTypeOrderByCreatedAtDesc(
            userId, userType, pageable);
        return notifications.map(this::mapToResponseDto);
    }

    @Transactional(readOnly = true)
    public Long getUnreadCount(Long userId, String userType) {
        return notificationRepository.countByUserIdAndUserTypeAndIsReadFalse(userId, userType);
    }

    public void markAsRead(Long notificationId, Long userId) {
        int updated = notificationRepository.markAsRead(notificationId, userId);
        if (updated == 0) {
            throw new IllegalArgumentException(
                "Notification not found with id: " + notificationId + " for user: " + userId);
        }
    }

    public void markAllAsRead(Long userId, String userType) {
        notificationRepository.markAllAsRead(userId, userType);
    }

    @Transactional(readOnly = true)
    public NotificationResponseDto getNotificationById(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Notification not found with id: " + notificationId));
        return mapToResponseDto(notification);
    }

    private NotificationResponseDto mapToResponseDto(Notification notification) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setUserType(notification.getUserType());
        dto.setType(notification.getType());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setData(notification.getData());
        dto.setIsRead(notification.getIsRead());
        dto.setReadAt(notification.getReadAt());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setUpdatedAt(notification.getUpdatedAt());

        // Parse attachments from JSON string
        if (notification.getAttachments() != null && !notification.getAttachments().isEmpty()) {
            try {
                List<AttachmentDto> attachments = objectMapper.readValue(
                    notification.getAttachments(), 
                    new TypeReference<List<AttachmentDto>>() {}
                );
                dto.setAttachments(attachments);
            } catch (JsonProcessingException e) {
                log.error("Failed to deserialize notification attachments", e);
                dto.setAttachments(new ArrayList<>());
            }
        } else {
            dto.setAttachments(new ArrayList<>());
        }

        return dto;
    }
}

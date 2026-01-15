package com.school.erp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.erp.dto.AttachmentDto;
import com.school.erp.dto.NotificationResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseNotificationService {

    // Store active SSE connections: key = "userId:userType", value = SseEmitter
    private final Map<String, SseEmitter> activeConnections = new ConcurrentHashMap<>();

    private static final long SSE_TIMEOUT = 30 * 60 * 1000L; // 30 minutes

    public SseEmitter subscribe(Long userId, String userType) {
        String connectionKey = getConnectionKey(userId, userType);
        
        // Remove existing connection if any
        SseEmitter existing = activeConnections.remove(connectionKey);
        if (existing != null) {
            try {
                existing.complete();
            } catch (Exception e) {
                log.debug("Error completing existing SSE connection", e);
            }
        }

        // Create new SSE emitter
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        
        // Handle completion
        emitter.onCompletion(() -> {
            log.debug("SSE connection completed for user {} ({})", userId, userType);
            activeConnections.remove(connectionKey);
        });

        // Handle timeout
        emitter.onTimeout(() -> {
            log.debug("SSE connection timeout for user {} ({})", userId, userType);
            activeConnections.remove(connectionKey);
            try {
                emitter.complete();
            } catch (Exception e) {
                log.debug("Error completing SSE connection on timeout", e);
            }
        });

        // Handle error
        emitter.onError((ex) -> {
            log.error("SSE connection error for user {} ({}): {}", userId, userType, ex.getMessage());
            activeConnections.remove(connectionKey);
            try {
                emitter.completeWithError(ex);
            } catch (Exception e) {
                log.debug("Error completing SSE connection on error", e);
            }
        });

        // Send initial connection event
        try {
            emitter.send(SseEmitter.event()
                .name("connected")
                .data("SSE connection established"));
        } catch (IOException e) {
            log.error("Failed to send initial SSE event", e);
            emitter.completeWithError(e);
            return emitter;
        }

        activeConnections.put(connectionKey, emitter);
        log.info("SSE connection established for user {} ({})", userId, userType);
        
        return emitter;
    }

    public void sendToUser(Long userId, String userType, com.school.erp.domain.entity.Notification notification) {
        String connectionKey = getConnectionKey(userId, userType);
        SseEmitter emitter = activeConnections.get(connectionKey);

        if (emitter != null) {
            try {
                // Convert entity to DTO for serialization
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
                        ObjectMapper objectMapper = new ObjectMapper();
                        List<AttachmentDto> attachments = objectMapper.readValue(
                            notification.getAttachments(), 
                            new TypeReference<List<AttachmentDto>>() {}
                        );
                        dto.setAttachments(attachments);
                    } catch (Exception e) {
                        log.error("Failed to deserialize attachments for SSE", e);
                        dto.setAttachments(new ArrayList<>());
                    }
                } else {
                    dto.setAttachments(new ArrayList<>());
                }
                
                emitter.send(SseEmitter.event()
                    .name("notification")
                    .data(dto));
                log.debug("Notification sent via SSE to user {} ({})", userId, userType);
            } catch (IOException e) {
                log.error("Failed to send notification via SSE to user {} ({}): {}", 
                    userId, userType, e.getMessage());
                // Remove failed connection
                activeConnections.remove(connectionKey);
                try {
                    emitter.completeWithError(e);
                } catch (Exception ex) {
                    log.debug("Error completing failed SSE connection", ex);
                }
            }
        } else {
            log.debug("No active SSE connection for user {} ({})", userId, userType);
        }
    }

    public void disconnect(Long userId, String userType) {
        String connectionKey = getConnectionKey(userId, userType);
        SseEmitter emitter = activeConnections.remove(connectionKey);
        
        if (emitter != null) {
            try {
                emitter.complete();
                log.debug("SSE connection disconnected for user {} ({})", userId, userType);
            } catch (Exception e) {
                log.debug("Error completing SSE connection on disconnect", e);
            }
        }
    }

    private String getConnectionKey(Long userId, String userType) {
        return userId + ":" + userType;
    }

    public int getActiveConnectionCount() {
        return activeConnections.size();
    }
}

package com.school.erp.controller;

import com.school.erp.dto.NotificationResponseDto;
import com.school.erp.security.UserDetailsImpl;
import com.school.erp.service.NotificationService;
import com.school.erp.service.SseNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SseNotificationService sseNotificationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<Page<NotificationResponseDto>> getNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            String userType = userDetails.getRole().equals("ROLE_TEACHER") ? "TEACHER" : "STUDENT";
            Pageable pageable = PageRequest.of(page, size);
            Page<NotificationResponseDto> notifications = notificationService.getUserNotifications(
                userDetails.getUserId(), userType, pageable);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/unread-count")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<Long> getUnreadCount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            String userType = userDetails.getRole().equals("ROLE_TEACHER") ? "TEACHER" : "STUDENT";
            Long count = notificationService.getUnreadCount(userDetails.getUserId(), userType);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            notificationService.markAsRead(id, userDetails.getUserId());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/read-all")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            String userType = userDetails.getRole().equals("ROLE_TEACHER") ? "TEACHER" : "STUDENT";
            notificationService.markAllAsRead(userDetails.getUserId(), userType);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public SseEmitter streamNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userType = userDetails.getRole().equals("ROLE_TEACHER") ? "TEACHER" : "STUDENT";
        return sseNotificationService.subscribe(userDetails.getUserId(), userType);
    }
}

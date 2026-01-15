package com.school.erp.controller;

import com.school.erp.dto.AnnouncementResponseDto;
import com.school.erp.dto.CreateAnnouncementDto;
import com.school.erp.security.UserDetailsImpl;
import com.school.erp.service.AnnouncementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
@Validated
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AnnouncementResponseDto> createAnnouncement(
            @Valid @RequestBody CreateAnnouncementDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            AnnouncementResponseDto announcement = announcementService.createAnnouncement(dto, userDetails.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(announcement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<AnnouncementResponseDto>> getMyAnnouncements(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<AnnouncementResponseDto> announcements = announcementService.getMyAnnouncements(userDetails.getUserId());
            return ResponseEntity.ok(announcements);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

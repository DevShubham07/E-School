package com.school.erp.controller;

import com.school.erp.dto.ComplaintResponseDto;
import com.school.erp.dto.CreateComplaintDto;
import com.school.erp.dto.UpdateComplaintStatusDto;
import com.school.erp.security.UserDetailsImpl;
import com.school.erp.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/complaints")
@RequiredArgsConstructor
@Validated
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ComplaintResponseDto> createComplaint(
            @Valid @RequestBody CreateComplaintDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            ComplaintResponseDto complaint = complaintService.createComplaint(dto, userDetails.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(complaint);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ComplaintResponseDto> updateComplaintStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateComplaintStatusDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            ComplaintResponseDto complaint = complaintService.updateComplaintStatus(id, dto, userDetails.getUserId());
            return ResponseEntity.ok(complaint);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ComplaintResponseDto>> getMyComplaints(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ComplaintResponseDto> complaints = complaintService.getMyComplaints(userDetails.getUserId());
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<ComplaintResponseDto> getComplaint(@PathVariable Long id) {
        try {
            ComplaintResponseDto complaint = complaintService.getComplaintById(id);
            return ResponseEntity.ok(complaint);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

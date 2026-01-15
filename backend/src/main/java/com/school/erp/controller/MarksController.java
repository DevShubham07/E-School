package com.school.erp.controller;

import com.school.erp.dto.CreateMarksDto;
import com.school.erp.dto.MarksResponseDto;
import com.school.erp.security.UserDetailsImpl;
import com.school.erp.service.MarksService;
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
@RequestMapping("/marks")
@RequiredArgsConstructor
@Validated
public class MarksController {

    private final MarksService marksService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<MarksResponseDto> createOrUpdateMarks(
            @Valid @RequestBody CreateMarksDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            MarksResponseDto marks = marksService.createOrUpdateMarks(dto, userDetails.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(marks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/exam/{examId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<MarksResponseDto>> getMarksByExam(@PathVariable Long examId) {
        List<MarksResponseDto> marksList = marksService.getMarksByExam(examId);
        return ResponseEntity.ok(marksList);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<MarksResponseDto>> getMyMarks(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<MarksResponseDto> marksList = marksService.getMyMarks(userDetails.getUserId());
        return ResponseEntity.ok(marksList);
    }
}

package com.school.erp.controller;

import com.school.erp.dto.CreateExamDto;
import com.school.erp.dto.ExamResponseDto;
import com.school.erp.security.UserDetailsImpl;
import com.school.erp.service.ExamService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
@Validated
public class ExamController {

    private final ExamService examService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExamResponseDto> createExam(
            @Valid @RequestBody CreateExamDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            ExamResponseDto exam = examService.createExam(dto, userDetails.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(exam);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<ExamResponseDto> getExam(@PathVariable Long id) {
        try {
            ExamResponseDto exam = examService.getExamById(id);
            return ResponseEntity.ok(exam);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/class/{classSectionId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<ExamResponseDto>> getExamsByClassSection(
            @PathVariable Long classSectionId) {
        List<ExamResponseDto> exams = examService.getExamsByClassSection(classSectionId);
        return ResponseEntity.ok(exams);
    }
}

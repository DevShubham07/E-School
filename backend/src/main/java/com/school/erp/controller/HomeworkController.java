package com.school.erp.controller;

import com.school.erp.dto.CreateHomeworkDto;
import com.school.erp.dto.HomeworkResponseDto;
import com.school.erp.dto.HomeworkSubmissionResponseDto;
import com.school.erp.dto.SubmitHomeworkDto;
import com.school.erp.dto.UpdateHomeworkDto;
import com.school.erp.security.UserDetailsImpl;
import com.school.erp.service.HomeworkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/homework")
@RequiredArgsConstructor
@Validated
public class HomeworkController {

    private final HomeworkService homeworkService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<HomeworkResponseDto> createHomework(
            @Valid @RequestBody CreateHomeworkDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            HomeworkResponseDto homework = homeworkService.createHomework(dto, userDetails.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(homework);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<HomeworkResponseDto> getHomework(@PathVariable Long id) {
        try {
            HomeworkResponseDto homework = homeworkService.getHomeworkById(id);
            return ResponseEntity.ok(homework);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/class/{classSectionId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<HomeworkResponseDto>> getHomeworksByClassSection(
            @PathVariable Long classSectionId) {
        List<HomeworkResponseDto> homeworks = homeworkService.getHomeworksByClassSection(classSectionId);
        return ResponseEntity.ok(homeworks);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<HomeworkResponseDto>> getMyHomeworks(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<HomeworkResponseDto> homeworks = homeworkService.getMyHomeworks(userDetails.getUserId());
            return ResponseEntity.ok(homeworks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<HomeworkResponseDto> updateHomework(
            @PathVariable Long id,
            @Valid @RequestBody UpdateHomeworkDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            HomeworkResponseDto homework = homeworkService.updateHomework(id, dto, userDetails.getUserId());
            return ResponseEntity.ok(homework);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteHomework(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            homeworkService.deleteHomework(id, userDetails.getUserId());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<HomeworkSubmissionResponseDto> submitHomework(
            @Valid @RequestBody SubmitHomeworkDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            HomeworkSubmissionResponseDto submission = homeworkService.submitHomework(dto, userDetails.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(submission);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/submissions")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<HomeworkSubmissionResponseDto>> getSubmissionsByHomework(
            @PathVariable Long id) {
        List<HomeworkSubmissionResponseDto> submissions = homeworkService.getSubmissionsByHomework(id);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/my-submissions")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<HomeworkSubmissionResponseDto>> getMySubmissions(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<HomeworkSubmissionResponseDto> submissions = homeworkService.getMySubmissions(userDetails.getUserId());
        return ResponseEntity.ok(submissions);
    }
}

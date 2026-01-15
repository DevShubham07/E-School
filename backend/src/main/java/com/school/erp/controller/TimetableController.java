package com.school.erp.controller;

import com.school.erp.dto.CreateTimetableEntryDto;
import com.school.erp.dto.TimetableEntryResponseDto;
import com.school.erp.dto.UpdateTimetableEntryDto;
import com.school.erp.security.UserDetailsImpl;
import com.school.erp.service.TimetableService;
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
@RequestMapping("/timetable")
@RequiredArgsConstructor
@Validated
public class TimetableController {

    private final TimetableService timetableService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<TimetableEntryResponseDto> createTimetableEntry(
            @Valid @RequestBody CreateTimetableEntryDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            TimetableEntryResponseDto entry = timetableService.createTimetableEntry(dto, userDetails.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(entry);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<TimetableEntryResponseDto> updateTimetableEntry(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTimetableEntryDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            TimetableEntryResponseDto entry = timetableService.updateTimetableEntry(id, dto, userDetails.getUserId());
            return ResponseEntity.ok(entry);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteTimetableEntry(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            timetableService.deleteTimetableEntry(id, userDetails.getUserId());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/class/{classSectionId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<List<TimetableEntryResponseDto>> getTimetableByClassSection(
            @PathVariable Long classSectionId) {
        List<TimetableEntryResponseDto> entries = timetableService.getTimetableByClassSection(classSectionId);
        return ResponseEntity.ok(entries);
    }
}

package com.school.erp.controller;

import com.school.erp.dto.CreateStudyMaterialDto;
import com.school.erp.dto.StudyMaterialResponseDto;
import com.school.erp.security.UserDetailsImpl;
import com.school.erp.service.StudyMaterialService;
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
@RequestMapping("/study-material")
@RequiredArgsConstructor
@Validated
public class StudyMaterialController {

    private final StudyMaterialService studyMaterialService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<StudyMaterialResponseDto> createStudyMaterial(
            @Valid @RequestBody CreateStudyMaterialDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            StudyMaterialResponseDto material = studyMaterialService.createStudyMaterial(dto, userDetails.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(material);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<StudyMaterialResponseDto>> getMyStudyMaterials(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<StudyMaterialResponseDto> materials = studyMaterialService.getMyStudyMaterials(userDetails.getUserId());
            return ResponseEntity.ok(materials);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    public ResponseEntity<StudyMaterialResponseDto> getStudyMaterial(@PathVariable Long id) {
        try {
            StudyMaterialResponseDto material = studyMaterialService.getStudyMaterialById(id);
            return ResponseEntity.ok(material);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.school.erp.controller;

import com.school.erp.domain.entity.ClassSection;
import com.school.erp.service.ClassSectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/class-sections")
@RequiredArgsConstructor
@Validated
public class ClassSectionController {

    private final ClassSectionService classSectionService;

    @PostMapping
    public ResponseEntity<ClassSection> create(@Valid @RequestBody ClassSection classSection) {
        ClassSection created = classSectionService.create(classSection);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ClassSection>> getAll() {
        List<ClassSection> classSections = classSectionService.findAll();
        return ResponseEntity.ok(classSections);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassSection> getById(@PathVariable Long id) {
        return classSectionService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassSection> update(
            @PathVariable Long id,
            @Valid @RequestBody ClassSection classSection) {
        try {
            ClassSection updated = classSectionService.update(id, classSection);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            classSectionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

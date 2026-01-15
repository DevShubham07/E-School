package com.school.erp.controller;

import com.school.erp.domain.entity.Student;
import com.school.erp.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Validated
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Student> create(@Valid @RequestBody Student student) {
        Student created = studentService.create(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAll(
            @RequestParam(required = false) Long classSectionId,
            @RequestParam(required = false) Boolean isActive) {
        List<Student> students;
        
        if (classSectionId != null) {
            students = studentService.findByClassSectionId(classSectionId);
        } else if (isActive != null) {
            students = studentService.findByIsActive(isActive);
        } else {
            students = studentService.findAll();
        }
        
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return studentService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admission-number/{admissionNumber}")
    public ResponseEntity<Student> getByAdmissionNumber(@PathVariable String admissionNumber) {
        return studentService.findByAdmissionNumber(admissionNumber)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Student> update(
            @PathVariable Long id,
            @Valid @RequestBody Student student) {
        try {
            Student updated = studentService.update(id, student);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/assign-class-section")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Student> assignToClassSection(
            @PathVariable Long id,
            @RequestBody Map<String, Long> request) {
        try {
            Long classSectionId = request.get("classSectionId");
            if (classSectionId == null) {
                return ResponseEntity.badRequest().build();
            }
            Student updated = studentService.assignToClassSection(id, classSectionId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Student> activate(@PathVariable Long id) {
        try {
            Student updated = studentService.activate(id);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Student> deactivate(@PathVariable Long id) {
        try {
            Student updated = studentService.deactivate(id);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            studentService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

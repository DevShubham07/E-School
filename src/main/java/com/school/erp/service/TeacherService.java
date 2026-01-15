package com.school.erp.service;

import com.school.erp.domain.entity.Teacher;
import com.school.erp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public Teacher create(Teacher teacher) {
        // Check if employee code already exists
        if (teacherRepository.existsByEmployeeCode(teacher.getEmployeeCode())) {
            throw new IllegalArgumentException(
                String.format("Teacher with employee code '%s' already exists", teacher.getEmployeeCode()));
        }

        // Check if email already exists
        if (teacherRepository.existsByEmail(teacher.getEmail())) {
            throw new IllegalArgumentException(
                String.format("Teacher with email '%s' already exists", teacher.getEmail()));
        }

        // Set default values if null
        if (teacher.getIsCoordinator() == null) {
            teacher.setIsCoordinator(false);
        }
        if (teacher.getIsActive() == null) {
            teacher.setIsActive(true);
        }

        return teacherRepository.save(teacher);
    }

    @Transactional(readOnly = true)
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Teacher> findByEmployeeCode(String employeeCode) {
        return teacherRepository.findByEmployeeCode(employeeCode);
    }

    @Transactional(readOnly = true)
    public Optional<Teacher> findByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }

    public Teacher update(Long id, Teacher updatedTeacher) {
        Teacher existing = teacherRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + id));

        // Check if the new employee code conflicts with another record
        Optional<Teacher> existingWithEmployeeCode = teacherRepository
            .findByEmployeeCode(updatedTeacher.getEmployeeCode());
        
        if (existingWithEmployeeCode.isPresent() && !existingWithEmployeeCode.get().getId().equals(id)) {
            throw new IllegalArgumentException(
                String.format("Teacher with employee code '%s' already exists", updatedTeacher.getEmployeeCode()));
        }

        // Check if the new email conflicts with another record
        Optional<Teacher> existingWithEmail = teacherRepository
            .findByEmail(updatedTeacher.getEmail());
        
        if (existingWithEmail.isPresent() && !existingWithEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException(
                String.format("Teacher with email '%s' already exists", updatedTeacher.getEmail()));
        }

        // Update fields
        existing.setEmployeeCode(updatedTeacher.getEmployeeCode());
        existing.setName(updatedTeacher.getName());
        existing.setEmail(updatedTeacher.getEmail());
        existing.setPhone(updatedTeacher.getPhone());
        existing.setIsCoordinator(updatedTeacher.getIsCoordinator() != null 
            ? updatedTeacher.getIsCoordinator() : false);
        existing.setIsActive(updatedTeacher.getIsActive() != null 
            ? updatedTeacher.getIsActive() : true);
        
        return teacherRepository.save(existing);
    }

    public void deleteById(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new IllegalArgumentException("Teacher not found with id: " + id);
        }
        teacherRepository.deleteById(id);
    }
}

package com.school.erp.service;

import com.school.erp.domain.entity.ClassSection;
import com.school.erp.domain.entity.Student;
import com.school.erp.repository.ClassSectionRepository;
import com.school.erp.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClassSectionRepository classSectionRepository;

    public Student create(Student student) {
        // Check if admission number already exists
        if (studentRepository.existsByAdmissionNumber(student.getAdmissionNumber())) {
            throw new IllegalArgumentException(
                String.format("Student with admission number '%s' already exists", student.getAdmissionNumber()));
        }

        // Validate ClassSection if provided
        if (student.getClassSection() != null && student.getClassSection().getId() != null) {
            ClassSection classSection = classSectionRepository.findById(student.getClassSection().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Class section not found with id: " + student.getClassSection().getId()));
            student.setClassSection(classSection);
        }

        // Set default value if null
        if (student.getIsActive() == null) {
            student.setIsActive(true);
        }

        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Student> findByAdmissionNumber(String admissionNumber) {
        return studentRepository.findByAdmissionNumber(admissionNumber);
    }

    @Transactional(readOnly = true)
    public List<Student> findByClassSectionId(Long classSectionId) {
        return studentRepository.findByClassSectionId(classSectionId);
    }

    @Transactional(readOnly = true)
    public List<Student> findByIsActive(Boolean isActive) {
        return studentRepository.findByIsActive(isActive);
    }

    public Student update(Long id, Student updatedStudent) {
        Student existing = studentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        // Check if the new admission number conflicts with another record
        Optional<Student> existingWithAdmissionNumber = studentRepository
            .findByAdmissionNumber(updatedStudent.getAdmissionNumber());
        
        if (existingWithAdmissionNumber.isPresent() && !existingWithAdmissionNumber.get().getId().equals(id)) {
            throw new IllegalArgumentException(
                String.format("Student with admission number '%s' already exists", updatedStudent.getAdmissionNumber()));
        }

        // Update fields
        existing.setAdmissionNumber(updatedStudent.getAdmissionNumber());
        existing.setName(updatedStudent.getName());
        existing.setEmail(updatedStudent.getEmail());
        existing.setPhone(updatedStudent.getPhone());
        existing.setRollNumber(updatedStudent.getRollNumber());
        existing.setIsActive(updatedStudent.getIsActive() != null 
            ? updatedStudent.getIsActive() : true);

        // Update ClassSection if provided
        if (updatedStudent.getClassSection() != null && updatedStudent.getClassSection().getId() != null) {
            ClassSection classSection = classSectionRepository.findById(updatedStudent.getClassSection().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Class section not found with id: " + updatedStudent.getClassSection().getId()));
            existing.setClassSection(classSection);
        } else if (updatedStudent.getClassSection() == null) {
            // Allow removing class section assignment
            existing.setClassSection(null);
        }

        return studentRepository.save(existing);
    }

    public Student assignToClassSection(Long studentId, Long classSectionId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        ClassSection classSection = classSectionRepository.findById(classSectionId)
            .orElseThrow(() -> new IllegalArgumentException("Class section not found with id: " + classSectionId));

        student.setClassSection(classSection);
        return studentRepository.save(student);
    }

    public Student activate(Long id) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        student.setIsActive(true);
        return studentRepository.save(student);
    }

    public Student deactivate(Long id) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        student.setIsActive(false);
        return studentRepository.save(student);
    }

    public void deleteById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}

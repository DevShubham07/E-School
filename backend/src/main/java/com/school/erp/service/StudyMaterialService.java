package com.school.erp.service;

import com.school.erp.domain.entity.ClassSection;
import com.school.erp.domain.entity.Student;
import com.school.erp.domain.entity.StudyMaterial;
import com.school.erp.domain.entity.Teacher;
import com.school.erp.dto.CreateStudyMaterialDto;
import com.school.erp.dto.StudyMaterialResponseDto;
import com.school.erp.repository.ClassSectionRepository;
import com.school.erp.repository.StudentRepository;
import com.school.erp.repository.StudyMaterialRepository;
import com.school.erp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyMaterialService {

    private final StudyMaterialRepository studyMaterialRepository;
    private final ClassSectionRepository classSectionRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public StudyMaterialResponseDto createStudyMaterial(CreateStudyMaterialDto dto, Long teacherId) {
        // Validate class section exists
        ClassSection classSection = classSectionRepository.findById(dto.getClassSectionId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Class section not found with id: " + dto.getClassSectionId()));

        // Validate teacher exists
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));

        // Create study material
        StudyMaterial studyMaterial = new StudyMaterial();
        studyMaterial.setTitle(dto.getTitle());
        studyMaterial.setDescription(dto.getDescription());
        studyMaterial.setSubject(dto.getSubject());
        studyMaterial.setClassSection(classSection);
        studyMaterial.setFileUrl(dto.getFileUrl());
        studyMaterial.setUploadedByTeacher(teacher);

        studyMaterial = studyMaterialRepository.save(studyMaterial);

        return mapToResponseDto(studyMaterial);
    }

    @Transactional(readOnly = true)
    public List<StudyMaterialResponseDto> getMyStudyMaterials(Long studentId) {
        // Get student's class section
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        if (student.getClassSection() == null) {
            throw new IllegalArgumentException("Student is not assigned to any class section");
        }

        // Get study materials for student's class section
        List<StudyMaterial> materials = studyMaterialRepository.findByClassSectionId(
            student.getClassSection().getId());

        return materials.stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudyMaterialResponseDto getStudyMaterialById(Long materialId) {
        StudyMaterial material = studyMaterialRepository.findById(materialId)
            .orElseThrow(() -> new IllegalArgumentException("Study material not found with id: " + materialId));
        return mapToResponseDto(material);
    }

    @Transactional(readOnly = true)
    public List<StudyMaterialResponseDto> getStudyMaterialsByClassSection(Long classSectionId) {
        List<StudyMaterial> materials = studyMaterialRepository.findByClassSectionId(classSectionId);
        return materials.stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    private StudyMaterialResponseDto mapToResponseDto(StudyMaterial material) {
        StudyMaterialResponseDto dto = new StudyMaterialResponseDto();
        dto.setId(material.getId());
        dto.setTitle(material.getTitle());
        dto.setDescription(material.getDescription());
        dto.setSubject(material.getSubject());
        dto.setClassSectionId(material.getClassSection().getId());
        dto.setClassName(material.getClassSection().getClassName());
        dto.setSectionName(material.getClassSection().getSectionName());
        dto.setFileUrl(material.getFileUrl());
        dto.setUploadedByTeacherId(material.getUploadedByTeacher().getId());
        dto.setTeacherName(material.getUploadedByTeacher().getName());
        dto.setCreatedAt(material.getCreatedAt());
        dto.setUpdatedAt(material.getUpdatedAt());
        return dto;
    }
}

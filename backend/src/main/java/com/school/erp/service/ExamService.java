package com.school.erp.service;

import com.school.erp.domain.entity.ClassSection;
import com.school.erp.domain.entity.Exam;
import com.school.erp.domain.entity.Teacher;
import com.school.erp.dto.CreateExamDto;
import com.school.erp.dto.ExamResponseDto;
import com.school.erp.repository.ClassSectionRepository;
import com.school.erp.repository.ExamRepository;
import com.school.erp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamService {

    private final ExamRepository examRepository;
    private final ClassSectionRepository classSectionRepository;
    private final TeacherRepository teacherRepository;

    public ExamResponseDto createExam(CreateExamDto dto, Long teacherId) {
        // Validate class section exists
        ClassSection classSection = classSectionRepository.findById(dto.getClassSectionId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Class section not found with id: " + dto.getClassSectionId()));

        // Validate teacher exists
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));

        // Create exam
        Exam exam = new Exam();
        exam.setName(dto.getName());
        exam.setSubject(dto.getSubject());
        exam.setExamDate(dto.getExamDate());
        exam.setClassSection(classSection);
        exam.setCreatedByTeacher(teacher);

        exam = examRepository.save(exam);

        return mapToExamResponseDto(exam);
    }

    @Transactional(readOnly = true)
    public ExamResponseDto getExamById(Long examId) {
        Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new IllegalArgumentException("Exam not found with id: " + examId));
        return mapToExamResponseDto(exam);
    }

    @Transactional(readOnly = true)
    public List<ExamResponseDto> getExamsByClassSection(Long classSectionId) {
        List<Exam> exams = examRepository.findByClassSectionId(classSectionId);
        return exams.stream()
            .map(this::mapToExamResponseDto)
            .collect(Collectors.toList());
    }

    private ExamResponseDto mapToExamResponseDto(Exam exam) {
        ExamResponseDto dto = new ExamResponseDto();
        dto.setId(exam.getId());
        dto.setName(exam.getName());
        dto.setSubject(exam.getSubject());
        dto.setExamDate(exam.getExamDate());
        dto.setClassSectionId(exam.getClassSection().getId());
        dto.setClassName(exam.getClassSection().getClassName());
        dto.setSectionName(exam.getClassSection().getSectionName());
        dto.setCreatedByTeacherId(exam.getCreatedByTeacher().getId());
        dto.setTeacherName(exam.getCreatedByTeacher().getName());
        dto.setCreatedAt(exam.getCreatedAt());
        dto.setUpdatedAt(exam.getUpdatedAt());
        return dto;
    }
}

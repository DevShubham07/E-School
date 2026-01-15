package com.school.erp.service;

import com.school.erp.domain.entity.Exam;
import com.school.erp.domain.entity.Marks;
import com.school.erp.domain.entity.Student;
import com.school.erp.domain.entity.Teacher;
import com.school.erp.dto.CreateMarksDto;
import com.school.erp.dto.MarksResponseDto;
import com.school.erp.repository.ExamRepository;
import com.school.erp.repository.MarksRepository;
import com.school.erp.repository.StudentRepository;
import com.school.erp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MarksService {

    private final MarksRepository marksRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final NotificationService notificationService;

    public MarksResponseDto createOrUpdateMarks(CreateMarksDto dto, Long teacherId) {
        // Validate exam exists (with class section loaded)
        Exam exam = examRepository.findByIdWithClassSection(dto.getExamId())
            .orElseThrow(() -> new IllegalArgumentException("Exam not found with id: " + dto.getExamId()));

        // Validate student exists (with class section loaded)
        Student student = studentRepository.findByIdWithClassSection(dto.getStudentId())
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + dto.getStudentId()));

        // Validate student belongs to the exam's class section
        if (student.getClassSection() == null || 
            !student.getClassSection().getId().equals(exam.getClassSection().getId())) {
            throw new IllegalArgumentException(
                "Student does not belong to the class section of this exam");
        }

        // Validate marks obtained is not greater than max marks
        if (dto.getMarksObtained().compareTo(dto.getMaxMarks()) > 0) {
            throw new IllegalArgumentException(
                "Marks obtained cannot be greater than maximum marks");
        }

        // Validate teacher exists
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));

        // Check if marks already exists
        Marks marks = marksRepository.findByExamIdAndStudentId(dto.getExamId(), dto.getStudentId())
            .orElse(null);

        if (marks != null) {
            // Update existing marks
            marks.setMarksObtained(dto.getMarksObtained());
            marks.setMaxMarks(dto.getMaxMarks());
            marks.setGradedByTeacher(teacher);
            marks = marksRepository.save(marks);
        } else {
            // Create new marks
            marks = new Marks();
            marks.setExam(exam);
            marks.setStudent(student);
            marks.setMarksObtained(dto.getMarksObtained());
            marks.setMaxMarks(dto.getMaxMarks());
            marks.setGradedByTeacher(teacher);
            marks = marksRepository.save(marks);
        }

        // Notify student about marks
        Map<String, Object> data = new HashMap<>();
        data.put("marksId", marks.getId());
        data.put("examId", exam.getId());
        data.put("examName", exam.getName());
        data.put("marksObtained", marks.getMarksObtained().toString());
        data.put("maxMarks", marks.getMaxMarks().toString());
        
        BigDecimal percentage = marks.getMarksObtained()
            .divide(marks.getMaxMarks(), 4, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"))
            .setScale(2, RoundingMode.HALF_UP);
        data.put("percentage", percentage.toString());
        
        notificationService.createNotification(
            student.getId(),
            "STUDENT",
            com.school.erp.domain.enums.NotificationType.MARKS,
            "Marks Published",
            String.format("Your marks for %s (%s) have been published. You scored %.2f%%", 
                exam.getName(), exam.getSubject(), percentage.doubleValue()),
            data
        );

        return mapToMarksResponseDto(marks);
    }

    @Transactional(readOnly = true)
    public List<MarksResponseDto> getMarksByExam(Long examId) {
        List<Marks> marksList = marksRepository.findByExamId(examId);
        return marksList.stream()
            .map(this::mapToMarksResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MarksResponseDto> getMyMarks(Long studentId) {
        List<Marks> marksList = marksRepository.findByStudentId(studentId);
        return marksList.stream()
            .map(this::mapToMarksResponseDto)
            .collect(Collectors.toList());
    }

    private MarksResponseDto mapToMarksResponseDto(Marks marks) {
        MarksResponseDto dto = new MarksResponseDto();
        dto.setId(marks.getId());
        dto.setExamId(marks.getExam().getId());
        dto.setExamName(marks.getExam().getName());
        dto.setSubject(marks.getExam().getSubject());
        dto.setExamDate(marks.getExam().getExamDate());
        dto.setStudentId(marks.getStudent().getId());
        dto.setStudentName(marks.getStudent().getName());
        dto.setAdmissionNumber(marks.getStudent().getAdmissionNumber());
        dto.setMarksObtained(marks.getMarksObtained());
        dto.setMaxMarks(marks.getMaxMarks());
        
        // Calculate percentage
        BigDecimal percentage = marks.getMarksObtained()
            .divide(marks.getMaxMarks(), 4, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"))
            .setScale(2, RoundingMode.HALF_UP);
        dto.setPercentage(percentage);
        
        dto.setGradedByTeacherId(marks.getGradedByTeacher().getId());
        dto.setTeacherName(marks.getGradedByTeacher().getName());
        dto.setCreatedAt(marks.getCreatedAt());
        dto.setUpdatedAt(marks.getUpdatedAt());
        return dto;
    }
}

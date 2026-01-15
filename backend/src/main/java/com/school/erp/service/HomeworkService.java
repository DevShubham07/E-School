package com.school.erp.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school.erp.domain.entity.ClassSection;
import com.school.erp.domain.entity.Homework;
import com.school.erp.domain.entity.HomeworkSubmission;
import com.school.erp.domain.entity.Student;
import com.school.erp.domain.entity.Teacher;
import com.school.erp.dto.CreateHomeworkDto;
import com.school.erp.dto.HomeworkResponseDto;
import com.school.erp.dto.HomeworkSubmissionResponseDto;
import com.school.erp.dto.SubmitHomeworkDto;
import com.school.erp.dto.UpdateHomeworkDto;
import com.school.erp.repository.ClassSectionRepository;
import com.school.erp.repository.HomeworkRepository;
import com.school.erp.repository.HomeworkSubmissionRepository;
import com.school.erp.repository.StudentRepository;
import com.school.erp.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final HomeworkSubmissionRepository homeworkSubmissionRepository;
    private final ClassSectionRepository classSectionRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final NotificationService notificationService;

    public HomeworkResponseDto createHomework(CreateHomeworkDto dto, Long teacherId) {
        // Validate class section exists
        ClassSection classSection = classSectionRepository.findById(dto.getClassSectionId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Class section not found with id: " + dto.getClassSectionId()));

        // Validate teacher exists
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));

        // Create homework
        Homework homework = new Homework();
        homework.setTitle(dto.getTitle());
        homework.setDescription(dto.getDescription());
        homework.setSubject(dto.getSubject());
        homework.setDueDate(dto.getDueDate());
        homework.setClassSection(classSection);
        homework.setAssignedByTeacher(teacher);

        homework = homeworkRepository.save(homework);

        // Notify all students in the class section
        List<Student> students = studentRepository.findByClassSectionId(classSection.getId());
        for (Student student : students) {
            if (student.getIsActive()) {
                Map<String, Object> data = new HashMap<>();
                data.put("homeworkId", homework.getId());
                data.put("classSectionId", classSection.getId());
                data.put("dueDate", homework.getDueDate().toString());
                
                notificationService.createNotification(
                    student.getId(),
                    "STUDENT",
                    com.school.erp.domain.enums.NotificationType.HOMEWORK,
                    "New Homework Assignment",
                    String.format("New homework '%s' has been assigned for %s. Due date: %s", 
                        homework.getTitle(), homework.getSubject(), homework.getDueDate()),
                    data
                );
            }
        }

        return mapToHomeworkResponseDto(homework);
    }

    @Transactional(readOnly = true)
    public HomeworkResponseDto getHomeworkById(Long homeworkId) {
        Homework homework = homeworkRepository.findByIdWithRelations(homeworkId)
            .orElseThrow(() -> new IllegalArgumentException("Homework not found with id: " + homeworkId));
        return mapToHomeworkResponseDto(homework);
    }

    @Transactional(readOnly = true)
    public List<HomeworkResponseDto> getHomeworksByClassSection(Long classSectionId) {
        List<Homework> homeworks = homeworkRepository.findByClassSectionIdWithRelations(classSectionId);
        return homeworks.stream()
            .map(this::mapToHomeworkResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HomeworkResponseDto> getMyHomeworks(Long studentId) {
        // Get student's class section
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        if (student.getClassSection() == null) {
            throw new IllegalArgumentException("Student is not assigned to any class section");
        }

        List<Homework> homeworks = homeworkRepository.findByClassSectionIdWithRelations(student.getClassSection().getId());
        return homeworks.stream()
            .map(this::mapToHomeworkResponseDto)
            .collect(Collectors.toList());
    }

    public HomeworkResponseDto updateHomework(Long homeworkId, UpdateHomeworkDto dto, Long teacherId) {
        Homework homework = homeworkRepository.findById(homeworkId)
            .orElseThrow(() -> new IllegalArgumentException("Homework not found with id: " + homeworkId));

        // Validate teacher is the one who assigned the homework
        if (!homework.getAssignedByTeacher().getId().equals(teacherId)) {
            throw new IllegalArgumentException("Only the teacher who assigned the homework can update it");
        }

        // Update fields if provided
        if (dto.getTitle() != null && !dto.getTitle().isEmpty()) {
            homework.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            homework.setDescription(dto.getDescription());
        }
        if (dto.getSubject() != null && !dto.getSubject().isEmpty()) {
            homework.setSubject(dto.getSubject());
        }
        if (dto.getDueDate() != null) {
            homework.setDueDate(dto.getDueDate());
        }

        homework = homeworkRepository.save(homework);

        return mapToHomeworkResponseDto(homework);
    }

    public void deleteHomework(Long homeworkId, Long teacherId) {
        Homework homework = homeworkRepository.findById(homeworkId)
            .orElseThrow(() -> new IllegalArgumentException("Homework not found with id: " + homeworkId));

        // Validate teacher is the one who assigned the homework
        if (!homework.getAssignedByTeacher().getId().equals(teacherId)) {
            throw new IllegalArgumentException("Only the teacher who assigned the homework can delete it");
        }

        homeworkRepository.delete(homework);
    }

    public HomeworkSubmissionResponseDto submitHomework(SubmitHomeworkDto dto, Long studentId) {
        // Validate homework exists
        Homework homework = homeworkRepository.findById(dto.getHomeworkId())
            .orElseThrow(() -> new IllegalArgumentException("Homework not found with id: " + dto.getHomeworkId()));

        // Validate student exists
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        // Validate student belongs to the homework's class section
        if (student.getClassSection() == null || 
            !student.getClassSection().getId().equals(homework.getClassSection().getId())) {
            throw new IllegalArgumentException(
                "Student does not belong to the class section of this homework");
        }

        // Check if submission already exists
        HomeworkSubmission submission = homeworkSubmissionRepository
            .findByHomeworkIdAndStudentId(dto.getHomeworkId(), studentId)
            .orElse(null);

        if (submission != null) {
            // Update existing submission
            submission.setSubmissionText(dto.getSubmissionText());
            submission.setFileUrl(dto.getFileUrl());
            submission.setSubmittedAt(Instant.now());
            submission = homeworkSubmissionRepository.save(submission);
        } else {
            // Create new submission
            submission = new HomeworkSubmission();
            submission.setHomework(homework);
            submission.setStudent(student);
            submission.setSubmissionText(dto.getSubmissionText());
            submission.setFileUrl(dto.getFileUrl());
            submission.setSubmittedAt(Instant.now());
            submission = homeworkSubmissionRepository.save(submission);
        }

        return mapToSubmissionResponseDto(submission);
    }

    @Transactional(readOnly = true)
    public List<HomeworkSubmissionResponseDto> getSubmissionsByHomework(Long homeworkId) {
        List<HomeworkSubmission> submissions = homeworkSubmissionRepository.findByHomeworkId(homeworkId);
        return submissions.stream()
            .map(this::mapToSubmissionResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HomeworkSubmissionResponseDto> getMySubmissions(Long studentId) {
        List<HomeworkSubmission> submissions = homeworkSubmissionRepository.findByStudentId(studentId);
        return submissions.stream()
            .map(this::mapToSubmissionResponseDto)
            .collect(Collectors.toList());
    }

    private HomeworkResponseDto mapToHomeworkResponseDto(Homework homework) {
        HomeworkResponseDto dto = new HomeworkResponseDto();
        dto.setId(homework.getId());
        dto.setTitle(homework.getTitle());
        dto.setDescription(homework.getDescription());
        dto.setSubject(homework.getSubject());
        dto.setDueDate(homework.getDueDate());
        dto.setClassSectionId(homework.getClassSection().getId());
        dto.setClassName(homework.getClassSection().getClassName());
        dto.setSectionName(homework.getClassSection().getSectionName());
        dto.setAssignedByTeacherId(homework.getAssignedByTeacher().getId());
        dto.setTeacherName(homework.getAssignedByTeacher().getName());
        dto.setCreatedAt(homework.getCreatedAt());
        dto.setUpdatedAt(homework.getUpdatedAt());
        return dto;
    }

    private HomeworkSubmissionResponseDto mapToSubmissionResponseDto(HomeworkSubmission submission) {
        HomeworkSubmissionResponseDto dto = new HomeworkSubmissionResponseDto();
        dto.setId(submission.getId());
        dto.setHomeworkId(submission.getHomework().getId());
        dto.setHomeworkTitle(submission.getHomework().getTitle());
        dto.setStudentId(submission.getStudent().getId());
        dto.setStudentName(submission.getStudent().getName());
        dto.setAdmissionNumber(submission.getStudent().getAdmissionNumber());
        dto.setSubmissionText(submission.getSubmissionText());
        dto.setFileUrl(submission.getFileUrl());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setCreatedAt(submission.getCreatedAt());
        dto.setUpdatedAt(submission.getUpdatedAt());
        return dto;
    }
}

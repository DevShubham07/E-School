package com.school.erp.service;

import com.school.erp.domain.entity.Announcement;
import com.school.erp.domain.entity.ClassSection;
import com.school.erp.domain.entity.Student;
import com.school.erp.domain.entity.Teacher;
import com.school.erp.dto.AnnouncementResponseDto;
import com.school.erp.dto.CreateAnnouncementDto;
import com.school.erp.repository.AnnouncementRepository;
import com.school.erp.repository.ClassSectionRepository;
import com.school.erp.repository.StudentRepository;
import com.school.erp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final ClassSectionRepository classSectionRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public AnnouncementResponseDto createAnnouncement(CreateAnnouncementDto dto, Long teacherId) {
        // Validate class section exists
        ClassSection classSection = classSectionRepository.findById(dto.getClassSectionId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Class section not found with id: " + dto.getClassSectionId()));

        // Validate teacher exists
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));

        // Create announcement
        Announcement announcement = new Announcement();
        announcement.setTitle(dto.getTitle());
        announcement.setMessage(dto.getMessage());
        announcement.setClassSection(classSection);
        announcement.setCreatedByTeacher(teacher);
        announcement.setExpiresAt(dto.getExpiresAt());

        announcement = announcementRepository.save(announcement);

        return mapToResponseDto(announcement);
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponseDto> getMyAnnouncements(Long studentId) {
        // Get student's class section
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        if (student.getClassSection() == null) {
            throw new IllegalArgumentException("Student is not assigned to any class section");
        }

        // Get active announcements for student's class section
        List<Announcement> announcements = announcementRepository.findActiveByClassSectionId(
            student.getClassSection().getId(), Instant.now());

        return announcements.stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponseDto> getAnnouncementsByClassSection(Long classSectionId) {
        List<Announcement> announcements = announcementRepository.findByClassSectionId(classSectionId);
        return announcements.stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    private AnnouncementResponseDto mapToResponseDto(Announcement announcement) {
        AnnouncementResponseDto dto = new AnnouncementResponseDto();
        dto.setId(announcement.getId());
        dto.setTitle(announcement.getTitle());
        dto.setMessage(announcement.getMessage());
        dto.setClassSectionId(announcement.getClassSection().getId());
        dto.setClassName(announcement.getClassSection().getClassName());
        dto.setSectionName(announcement.getClassSection().getSectionName());
        dto.setCreatedByTeacherId(announcement.getCreatedByTeacher().getId());
        dto.setTeacherName(announcement.getCreatedByTeacher().getName());
        dto.setExpiresAt(announcement.getExpiresAt());
        dto.setCreatedAt(announcement.getCreatedAt());
        dto.setUpdatedAt(announcement.getUpdatedAt());
        return dto;
    }
}

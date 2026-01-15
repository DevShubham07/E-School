package com.school.erp.service;

import com.school.erp.domain.entity.ClassSection;
import com.school.erp.domain.entity.Teacher;
import com.school.erp.domain.entity.TimetableEntry;
import com.school.erp.domain.enums.DayOfWeek;
import com.school.erp.dto.CreateTimetableEntryDto;
import com.school.erp.dto.TimetableEntryResponseDto;
import com.school.erp.dto.UpdateTimetableEntryDto;
import com.school.erp.repository.ClassSectionRepository;
import com.school.erp.repository.TeacherRepository;
import com.school.erp.repository.TimetableEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TimetableService {

    private final TimetableEntryRepository timetableEntryRepository;
    private final ClassSectionRepository classSectionRepository;
    private final TeacherRepository teacherRepository;

    public TimetableEntryResponseDto createTimetableEntry(CreateTimetableEntryDto dto, Long coordinatorTeacherId) {
        // Validate coordinator teacher
        Teacher coordinator = teacherRepository.findById(coordinatorTeacherId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + coordinatorTeacherId));

        if (!coordinator.getIsCoordinator()) {
            throw new IllegalArgumentException("Only coordinator teachers can create timetable entries");
        }

        // Check if entry already exists for this combination
        if (timetableEntryRepository.existsByClassSectionIdAndDayOfWeekAndPeriodNumber(
                dto.getClassSectionId(), dto.getDayOfWeek(), dto.getPeriodNumber())) {
            throw new IllegalArgumentException(
                String.format("Timetable entry already exists for class section %d, %s, period %d",
                    dto.getClassSectionId(), dto.getDayOfWeek(), dto.getPeriodNumber()));
        }

        // Validate class section exists
        ClassSection classSection = classSectionRepository.findById(dto.getClassSectionId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Class section not found with id: " + dto.getClassSectionId()));

        // Validate teacher exists
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + dto.getTeacherId()));

        // Create timetable entry
        TimetableEntry entry = new TimetableEntry();
        entry.setClassSection(classSection);
        entry.setDayOfWeek(dto.getDayOfWeek());
        entry.setPeriodNumber(dto.getPeriodNumber());
        entry.setSubject(dto.getSubject());
        entry.setTeacher(teacher);

        entry = timetableEntryRepository.save(entry);

        return mapToResponseDto(entry);
    }

    public TimetableEntryResponseDto updateTimetableEntry(Long entryId, UpdateTimetableEntryDto dto, Long coordinatorTeacherId) {
        // Validate coordinator teacher
        Teacher coordinator = teacherRepository.findById(coordinatorTeacherId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + coordinatorTeacherId));

        if (!coordinator.getIsCoordinator()) {
            throw new IllegalArgumentException("Only coordinator teachers can update timetable entries");
        }

        // Get existing entry
        TimetableEntry entry = timetableEntryRepository.findById(entryId)
            .orElseThrow(() -> new IllegalArgumentException("Timetable entry not found with id: " + entryId));

        // Check if updating to a combination that already exists (excluding current entry)
        DayOfWeek newDayOfWeek = dto.getDayOfWeek() != null ? dto.getDayOfWeek() : entry.getDayOfWeek();
        Integer newPeriodNumber = dto.getPeriodNumber() != null ? dto.getPeriodNumber() : entry.getPeriodNumber();

        if (!newDayOfWeek.equals(entry.getDayOfWeek()) || !newPeriodNumber.equals(entry.getPeriodNumber())) {
            if (timetableEntryRepository.existsByClassSectionIdAndDayOfWeekAndPeriodNumber(
                    entry.getClassSection().getId(), newDayOfWeek, newPeriodNumber)) {
                throw new IllegalArgumentException(
                    String.format("Timetable entry already exists for class section %d, %s, period %d",
                        entry.getClassSection().getId(), newDayOfWeek, newPeriodNumber));
            }
        }

        // Update fields if provided
        if (dto.getDayOfWeek() != null) {
            entry.setDayOfWeek(dto.getDayOfWeek());
        }
        if (dto.getPeriodNumber() != null) {
            entry.setPeriodNumber(dto.getPeriodNumber());
        }
        if (dto.getSubject() != null && !dto.getSubject().isEmpty()) {
            entry.setSubject(dto.getSubject());
        }
        if (dto.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + dto.getTeacherId()));
            entry.setTeacher(teacher);
        }

        entry = timetableEntryRepository.save(entry);

        return mapToResponseDto(entry);
    }

    public void deleteTimetableEntry(Long entryId, Long coordinatorTeacherId) {
        // Validate coordinator teacher
        Teacher coordinator = teacherRepository.findById(coordinatorTeacherId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + coordinatorTeacherId));

        if (!coordinator.getIsCoordinator()) {
            throw new IllegalArgumentException("Only coordinator teachers can delete timetable entries");
        }

        // Validate entry exists
        TimetableEntry entry = timetableEntryRepository.findById(entryId)
            .orElseThrow(() -> new IllegalArgumentException("Timetable entry not found with id: " + entryId));

        timetableEntryRepository.delete(entry);
    }

    @Transactional(readOnly = true)
    public List<TimetableEntryResponseDto> getTimetableByClassSection(Long classSectionId) {
        List<TimetableEntry> entries = timetableEntryRepository.findByClassSectionId(classSectionId);
        return entries.stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    private TimetableEntryResponseDto mapToResponseDto(TimetableEntry entry) {
        TimetableEntryResponseDto dto = new TimetableEntryResponseDto();
        dto.setId(entry.getId());
        dto.setClassSectionId(entry.getClassSection().getId());
        dto.setClassName(entry.getClassSection().getClassName());
        dto.setSectionName(entry.getClassSection().getSectionName());
        dto.setDayOfWeek(entry.getDayOfWeek());
        dto.setPeriodNumber(entry.getPeriodNumber());
        dto.setSubject(entry.getSubject());
        dto.setTeacherId(entry.getTeacher().getId());
        dto.setTeacherName(entry.getTeacher().getName());
        dto.setCreatedAt(entry.getCreatedAt());
        dto.setUpdatedAt(entry.getUpdatedAt());
        return dto;
    }
}

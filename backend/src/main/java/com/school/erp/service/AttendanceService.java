package com.school.erp.service;

import com.school.erp.domain.entity.AttendanceRecord;
import com.school.erp.domain.entity.AttendanceSession;
import com.school.erp.domain.entity.ClassSection;
import com.school.erp.domain.entity.Student;
import com.school.erp.domain.entity.Teacher;
import com.school.erp.domain.enums.AttendanceStatus;
import com.school.erp.dto.AttendanceRecordResponseDto;
import com.school.erp.dto.AttendanceSessionResponseDto;
import com.school.erp.dto.CreateAttendanceSessionDto;
import com.school.erp.dto.MarkAttendanceDto;
import com.school.erp.repository.AttendanceRecordRepository;
import com.school.erp.repository.AttendanceSessionRepository;
import com.school.erp.repository.ClassSectionRepository;
import com.school.erp.repository.StudentRepository;
import com.school.erp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceSessionRepository attendanceSessionRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final ClassSectionRepository classSectionRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public AttendanceSessionResponseDto createSession(CreateAttendanceSessionDto dto, Long teacherId) {
        // Check if session already exists for this date and class section
        if (attendanceSessionRepository.existsByAttendanceDateAndClassSectionId(
                dto.getAttendanceDate(), dto.getClassSectionId())) {
            throw new IllegalArgumentException(
                String.format("Attendance session already exists for date %s and class section %d",
                    dto.getAttendanceDate(), dto.getClassSectionId()));
        }

        // Validate class section exists
        ClassSection classSection = classSectionRepository.findById(dto.getClassSectionId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Class section not found with id: " + dto.getClassSectionId()));

        // Validate teacher exists
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));

        // Create session
        AttendanceSession session = new AttendanceSession();
        session.setAttendanceDate(dto.getAttendanceDate());
        session.setClassSection(classSection);
        session.setMarkedByTeacher(teacher);

        session = attendanceSessionRepository.save(session);

        return mapToSessionResponseDto(session);
    }

    public AttendanceRecordResponseDto markAttendance(MarkAttendanceDto dto, Long teacherId) {
        // Validate session exists
        AttendanceSession session = attendanceSessionRepository.findById(dto.getAttendanceSessionId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Attendance session not found with id: " + dto.getAttendanceSessionId()));

        // Validate teacher is the one who created the session
        if (!session.getMarkedByTeacher().getId().equals(teacherId)) {
            throw new IllegalArgumentException("Only the teacher who created the session can mark attendance");
        }

        // Validate student exists
        Student student = studentRepository.findById(dto.getStudentId())
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + dto.getStudentId()));

        // Validate student belongs to the session's class section
        if (student.getClassSection() == null || 
            !student.getClassSection().getId().equals(session.getClassSection().getId())) {
            throw new IllegalArgumentException(
                "Student does not belong to the class section of this attendance session");
        }

        // Check if record already exists
        AttendanceRecord record = attendanceRecordRepository
            .findByAttendanceSessionIdAndStudentId(dto.getAttendanceSessionId(), dto.getStudentId())
            .orElse(null);

        if (record != null) {
            // Update existing record
            record.setStatus(dto.getStatus());
            record = attendanceRecordRepository.save(record);
        } else {
            // Create new record
            record = new AttendanceRecord();
            record.setAttendanceSession(session);
            record.setStudent(student);
            record.setStatus(dto.getStatus());
            record = attendanceRecordRepository.save(record);
        }

        return mapToRecordResponseDto(record);
    }

    @Transactional(readOnly = true)
    public AttendanceSessionResponseDto getSessionById(Long sessionId) {
        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Attendance session not found with id: " + sessionId));
        return mapToSessionResponseDto(session);
    }

    @Transactional(readOnly = true)
    public List<AttendanceRecordResponseDto> getMyAttendance(Long studentId) {
        List<AttendanceRecord> records = attendanceRecordRepository.findByStudentId(studentId);
        return records.stream()
            .map(this::mapToRecordResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AttendanceRecordResponseDto> getSessionRecords(Long sessionId) {
        List<AttendanceRecord> records = attendanceRecordRepository.findByAttendanceSessionId(sessionId);
        return records.stream()
            .map(this::mapToRecordResponseDto)
            .collect(Collectors.toList());
    }

    private AttendanceSessionResponseDto mapToSessionResponseDto(AttendanceSession session) {
        AttendanceSessionResponseDto dto = new AttendanceSessionResponseDto();
        dto.setId(session.getId());
        dto.setAttendanceDate(session.getAttendanceDate());
        dto.setClassSectionId(session.getClassSection().getId());
        dto.setClassName(session.getClassSection().getClassName());
        dto.setSectionName(session.getClassSection().getSectionName());
        dto.setMarkedByTeacherId(session.getMarkedByTeacher().getId());
        dto.setTeacherName(session.getMarkedByTeacher().getName());
        dto.setCreatedAt(session.getCreatedAt());
        dto.setUpdatedAt(session.getUpdatedAt());
        return dto;
    }

    private AttendanceRecordResponseDto mapToRecordResponseDto(AttendanceRecord record) {
        AttendanceRecordResponseDto dto = new AttendanceRecordResponseDto();
        dto.setId(record.getId());
        dto.setAttendanceSessionId(record.getAttendanceSession().getId());
        dto.setAttendanceDate(record.getAttendanceSession().getAttendanceDate());
        dto.setStudentId(record.getStudent().getId());
        dto.setStudentName(record.getStudent().getName());
        dto.setAdmissionNumber(record.getStudent().getAdmissionNumber());
        dto.setStatus(record.getStatus());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setUpdatedAt(record.getUpdatedAt());
        return dto;
    }
}

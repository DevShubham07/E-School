package com.school.erp.service;

import com.school.erp.domain.entity.Complaint;
import com.school.erp.domain.entity.Student;
import com.school.erp.domain.entity.Teacher;
import com.school.erp.domain.enums.ComplaintStatus;
import com.school.erp.dto.ComplaintResponseDto;
import com.school.erp.dto.CreateComplaintDto;
import com.school.erp.dto.UpdateComplaintStatusDto;
import com.school.erp.repository.ComplaintRepository;
import com.school.erp.repository.StudentRepository;
import com.school.erp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final NotificationService notificationService;

    public ComplaintResponseDto createComplaint(CreateComplaintDto dto, Long teacherId) {
        // Validate student exists
        Student student = studentRepository.findById(dto.getStudentId())
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + dto.getStudentId()));

        // Validate teacher exists
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));

        // Create complaint
        Complaint complaint = new Complaint();
        complaint.setStudent(student);
        complaint.setTeacher(teacher);
        complaint.setTitle(dto.getTitle());
        complaint.setDescription(dto.getDescription());
        complaint.setStatus(ComplaintStatus.OPEN);

        complaint = complaintRepository.save(complaint);

        return mapToResponseDto(complaint);
    }

    public ComplaintResponseDto updateComplaintStatus(Long complaintId, UpdateComplaintStatusDto dto, Long teacherId) {
        // Validate teacher exists
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));

        // Get existing complaint
        Complaint complaint = complaintRepository.findById(complaintId)
            .orElseThrow(() -> new IllegalArgumentException("Complaint not found with id: " + complaintId));

        // Update status
        ComplaintStatus oldStatus = complaint.getStatus();
        complaint.setStatus(dto.getStatus());
        complaint = complaintRepository.save(complaint);

        // Notify student if status changed
        if (!oldStatus.equals(dto.getStatus())) {
            Map<String, Object> data = new HashMap<>();
            data.put("complaintId", complaint.getId());
            data.put("status", dto.getStatus().toString());
            
            notificationService.createNotification(
                complaint.getStudent().getId(),
                "STUDENT",
                com.school.erp.domain.enums.NotificationType.COMPLAINT,
                "Complaint Status Updated",
                String.format("Your complaint '%s' status has been updated to %s", 
                    complaint.getTitle(), dto.getStatus()),
                data
            );
        }

        return mapToResponseDto(complaint);
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponseDto> getMyComplaints(Long studentId) {
        List<Complaint> complaints = complaintRepository.findByStudentId(studentId);
        return complaints.stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ComplaintResponseDto getComplaintById(Long complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
            .orElseThrow(() -> new IllegalArgumentException("Complaint not found with id: " + complaintId));
        return mapToResponseDto(complaint);
    }

    private ComplaintResponseDto mapToResponseDto(Complaint complaint) {
        ComplaintResponseDto dto = new ComplaintResponseDto();
        dto.setId(complaint.getId());
        dto.setStudentId(complaint.getStudent().getId());
        dto.setStudentName(complaint.getStudent().getName());
        dto.setAdmissionNumber(complaint.getStudent().getAdmissionNumber());
        dto.setTeacherId(complaint.getTeacher().getId());
        dto.setTeacherName(complaint.getTeacher().getName());
        dto.setTitle(complaint.getTitle());
        dto.setDescription(complaint.getDescription());
        dto.setStatus(complaint.getStatus());
        dto.setCreatedAt(complaint.getCreatedAt());
        dto.setUpdatedAt(complaint.getUpdatedAt());
        return dto;
    }
}

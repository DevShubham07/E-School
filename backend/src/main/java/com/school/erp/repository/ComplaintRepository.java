package com.school.erp.repository;

import com.school.erp.domain.entity.Complaint;
import com.school.erp.domain.enums.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByStudentId(Long studentId);

    List<Complaint> findByTeacherId(Long teacherId);

    List<Complaint> findByStatus(ComplaintStatus status);
}

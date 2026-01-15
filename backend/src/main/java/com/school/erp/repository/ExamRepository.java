package com.school.erp.repository;

import com.school.erp.domain.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByClassSectionId(Long classSectionId);

    List<Exam> findByCreatedByTeacherId(Long teacherId);
}

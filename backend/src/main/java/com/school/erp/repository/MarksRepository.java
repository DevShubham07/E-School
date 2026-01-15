package com.school.erp.repository;

import com.school.erp.domain.entity.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {

    Optional<Marks> findByExamIdAndStudentId(Long examId, Long studentId);

    List<Marks> findByExamId(Long examId);

    List<Marks> findByStudentId(Long studentId);

    boolean existsByExamIdAndStudentId(Long examId, Long studentId);
}

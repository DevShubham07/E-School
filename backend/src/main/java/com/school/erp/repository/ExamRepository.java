package com.school.erp.repository;

import com.school.erp.domain.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT e FROM Exam e " +
           "LEFT JOIN FETCH e.classSection " +
           "WHERE e.id = :id")
    Optional<Exam> findByIdWithClassSection(@Param("id") Long id);

    List<Exam> findByClassSectionId(Long classSectionId);

    List<Exam> findByCreatedByTeacherId(Long teacherId);
}

package com.school.erp.repository;

import com.school.erp.domain.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    @Query("SELECT h FROM Homework h " +
           "LEFT JOIN FETCH h.classSection " +
           "LEFT JOIN FETCH h.assignedByTeacher " +
           "WHERE h.id = :id")
    Optional<Homework> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT h FROM Homework h " +
           "LEFT JOIN FETCH h.classSection " +
           "LEFT JOIN FETCH h.assignedByTeacher " +
           "WHERE h.classSection.id = :classSectionId")
    List<Homework> findByClassSectionIdWithRelations(@Param("classSectionId") Long classSectionId);

    List<Homework> findByClassSectionId(Long classSectionId);

    List<Homework> findByAssignedByTeacherId(Long teacherId);
}

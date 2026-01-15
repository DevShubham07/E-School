package com.school.erp.repository;

import com.school.erp.domain.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    List<Homework> findByClassSectionId(Long classSectionId);

    List<Homework> findByAssignedByTeacherId(Long teacherId);
}

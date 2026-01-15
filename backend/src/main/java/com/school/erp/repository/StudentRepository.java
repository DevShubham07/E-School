package com.school.erp.repository;

import com.school.erp.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s " +
           "LEFT JOIN FETCH s.classSection " +
           "WHERE s.id = :id")
    Optional<Student> findByIdWithClassSection(@Param("id") Long id);

    Optional<Student> findByAdmissionNumber(String admissionNumber);

    boolean existsByAdmissionNumber(String admissionNumber);

    List<Student> findByClassSectionId(Long classSectionId);

    List<Student> findByIsActive(Boolean isActive);

    Optional<Student> findByPhone(String phone);
}

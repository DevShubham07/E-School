package com.school.erp.repository;

import com.school.erp.domain.entity.ClassSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassSectionRepository extends JpaRepository<ClassSection, Long> {

    Optional<ClassSection> findByClassNameAndSectionName(String className, String sectionName);

    boolean existsByClassNameAndSectionName(String className, String sectionName);
}

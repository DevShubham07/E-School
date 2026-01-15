package com.school.erp.repository;

import com.school.erp.domain.entity.TimetableEntry;
import com.school.erp.domain.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimetableEntryRepository extends JpaRepository<TimetableEntry, Long> {

    Optional<TimetableEntry> findByClassSectionIdAndDayOfWeekAndPeriodNumber(
            Long classSectionId, DayOfWeek dayOfWeek, Integer periodNumber);

    List<TimetableEntry> findByClassSectionId(Long classSectionId);

    List<TimetableEntry> findByClassSectionIdAndDayOfWeek(Long classSectionId, DayOfWeek dayOfWeek);

    boolean existsByClassSectionIdAndDayOfWeekAndPeriodNumber(
            Long classSectionId, DayOfWeek dayOfWeek, Integer periodNumber);
}

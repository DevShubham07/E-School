package com.school.erp.repository;

import com.school.erp.domain.entity.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {

    Optional<AttendanceSession> findByAttendanceDateAndClassSectionId(LocalDate attendanceDate, Long classSectionId);

    boolean existsByAttendanceDateAndClassSectionId(LocalDate attendanceDate, Long classSectionId);
}

package com.school.erp.repository;

import com.school.erp.domain.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    Optional<AttendanceRecord> findByAttendanceSessionIdAndStudentId(Long attendanceSessionId, Long studentId);

    List<AttendanceRecord> findByAttendanceSessionId(Long attendanceSessionId);

    List<AttendanceRecord> findByStudentId(Long studentId);

    boolean existsByAttendanceSessionIdAndStudentId(Long attendanceSessionId, Long studentId);
}

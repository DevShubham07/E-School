package com.school.erp.repository;

import com.school.erp.domain.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    List<Announcement> findByClassSectionId(Long classSectionId);

    @Query("SELECT a FROM Announcement a WHERE a.classSection.id = :classSectionId " +
           "AND (a.expiresAt IS NULL OR a.expiresAt > :now) " +
           "ORDER BY a.createdAt DESC")
    List<Announcement> findActiveByClassSectionId(@Param("classSectionId") Long classSectionId, 
                                                   @Param("now") Instant now);

    List<Announcement> findByCreatedByTeacherId(Long teacherId);
}

package com.school.erp.repository;

import com.school.erp.domain.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserIdAndUserTypeOrderByCreatedAtDesc(
            Long userId, String userType, Pageable pageable);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.userType = :userType AND n.isRead = false")
    Long countByUserIdAndUserTypeAndIsReadFalse(@Param("userId") Long userId, @Param("userType") String userType);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP WHERE n.id = :notificationId AND n.userId = :userId")
    int markAsRead(@Param("notificationId") Long notificationId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP WHERE n.userId = :userId AND n.userType = :userType AND n.isRead = false")
    int markAllAsRead(@Param("userId") Long userId, @Param("userType") String userType);

    List<Notification> findByUserIdAndUserTypeAndIsReadFalseOrderByCreatedAtDesc(
            Long userId, String userType);
}

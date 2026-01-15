package com.school.erp.repository;

import com.school.erp.domain.entity.OtpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface OtpRequestRepository extends JpaRepository<OtpRequest, Long> {

    Optional<OtpRequest> findFirstByPhoneNumberAndIsUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
            String phoneNumber, Instant now);

    Optional<OtpRequest> findFirstByEmailAndIsUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
            String email, Instant now);

    @Modifying
    @Query("UPDATE OtpRequest o SET o.isUsed = true WHERE o.phoneNumber = ?1 AND o.isUsed = false")
    void markAllAsUsedByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("UPDATE OtpRequest o SET o.isUsed = true WHERE o.email = ?1 AND o.isUsed = false")
    void markAllAsUsedByEmail(String email);
}

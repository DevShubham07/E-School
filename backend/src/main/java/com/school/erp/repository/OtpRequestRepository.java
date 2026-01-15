package com.school.erp.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.school.erp.domain.entity.OtpRequest;

@Repository
public interface OtpRequestRepository extends JpaRepository<OtpRequest, Long> {

    Optional<OtpRequest> findFirstByPhoneNumberAndIsUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
            String phoneNumber, Instant now);

    @Modifying
    @Query("UPDATE OtpRequest o SET o.isUsed = true WHERE o.phoneNumber = ?1 AND o.isUsed = false")
    void markAllAsUsedByPhoneNumber(String phoneNumber);
}

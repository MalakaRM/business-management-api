package com.smartbusiness.businessmanagement.repository;

import com.smartbusiness.businessmanagement.entity.Purchase;
import com.smartbusiness.businessmanagement.entity.enums.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface PurchaseRepository
        extends JpaRepository<Purchase, Long> {

    Optional<Purchase> findByReferenceNumber(
            String referenceNumber
    );

    boolean existsByReferenceNumber(
            String referenceNumber
    );
    @Query("""
        SELECT COALESCE(SUM(p.totalAmount), 0)
        FROM Purchase p
        WHERE p.status = :status
        AND p.purchaseDate = :date
        """)
    BigDecimal getTotalByStatusAndDate(
            @Param("status") PurchaseStatus status,
            @Param("date") LocalDate date
    );

    @Query("""
        SELECT COUNT(p)
        FROM Purchase p
        WHERE p.status = :status
        AND p.purchaseDate BETWEEN :from AND :to
        """)
    long countByStatusAndDateBetween(
            @Param("status") PurchaseStatus status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query("""
        SELECT COALESCE(SUM(p.totalAmount), 0)
        FROM Purchase p
        WHERE p.status = :status
        AND p.purchaseDate BETWEEN :from AND :to
        """)
    BigDecimal getTotalByStatusAndDateBetween(
            @Param("status") PurchaseStatus status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
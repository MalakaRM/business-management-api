package com.smartbusiness.businessmanagement.repository;

import com.smartbusiness.businessmanagement.dto.response.MonthlyAmountResponse;
import com.smartbusiness.businessmanagement.entity.Purchase;
import com.smartbusiness.businessmanagement.entity.enums.PurchaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
    @Query(value = """
    SELECT
        TO_CHAR(p.purchase_date, 'YYYY-MM') AS month,
        COALESCE(SUM(p.total_amount), 0) AS amount
    FROM purchases p
    WHERE p.status = :status
    AND p.purchase_date BETWEEN :from AND :to
    GROUP BY TO_CHAR(p.purchase_date, 'YYYY-MM')
    ORDER BY TO_CHAR(p.purchase_date, 'YYYY-MM')
    """, nativeQuery = true)
    List<Object[]> getMonthlyPurchases(
            @Param("status") String status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    //purchase report
    @Query("""
    SELECT p
    FROM Purchase p
    LEFT JOIN p.supplier s
    WHERE p.purchaseDate BETWEEN :from AND :to
    AND (
        :search = ''
        OR LOWER(p.referenceNumber) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))
    )
    """)
    Page<Purchase> searchPurchaseReportByDateAndSearch(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
    SELECT p
    FROM Purchase p
    WHERE p.purchaseDate BETWEEN :from AND :to
    """)
    Page<Purchase> searchPurchaseReportByDate(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable
    );

    @Query("""
    SELECT p
    FROM Purchase p
    LEFT JOIN p.supplier s
    WHERE
        LOWER(p.referenceNumber) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<Purchase> searchPurchaseReportBySearch(
            @Param("search") String search,
            Pageable pageable
    );
}
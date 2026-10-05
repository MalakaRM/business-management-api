package com.smartbusiness.businessmanagement.repository;

import com.smartbusiness.businessmanagement.dto.response.MonthlyAmountResponse;
import com.smartbusiness.businessmanagement.entity.Order;
import com.smartbusiness.businessmanagement.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(
            String orderNumber
    );

    boolean existsByOrderNumber(String orderNumber);
    long countByStatus(OrderStatus status);
    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE o.status = :status
        AND o.orderDate = :date
        """)
    BigDecimal getTotalByStatusAndDate(
            @Param("status") OrderStatus status,
            @Param("date") LocalDate date
    );
    @Query("""
        SELECT COUNT(o)
        FROM Order o
        WHERE o.status = :status
        AND o.orderDate BETWEEN :from AND :to
        """)
    long countByStatusAndDateBetween(
            @Param("status") OrderStatus status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE o.status = :status
        AND o.orderDate BETWEEN :from AND :to
        """)
    BigDecimal getTotalByStatusAndDateBetween(
            @Param("status") OrderStatus status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
    @Query(value = """
    SELECT
        TO_CHAR(o.order_date, 'YYYY-MM') AS month,
        COALESCE(SUM(o.total_amount), 0) AS amount
    FROM orders o
    WHERE o.status = :status
    AND o.order_date BETWEEN :from AND :to
    GROUP BY TO_CHAR(o.order_date, 'YYYY-MM')
    ORDER BY TO_CHAR(o.order_date, 'YYYY-MM')
    """, nativeQuery = true)
    List<Object[]> getMonthlySales(
            @Param("status") String status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    //filter
    @Query("""
    SELECT o
    FROM Order o
    LEFT JOIN o.customer c
    WHERE o.orderDate BETWEEN :from AND :to
    AND (
        :search = ''
        OR LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))
    )
    """)
    Page<Order> searchSalesReportByDateAndSearch(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
    SELECT o
    FROM Order o
    WHERE o.orderDate BETWEEN :from AND :to
    """)
    Page<Order> searchSalesReportByDate(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable
    );

    @Query("""
    SELECT o
    FROM Order o
    LEFT JOIN o.customer c
    WHERE
        LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<Order> searchSalesReportBySearch(
            @Param("search") String search,
            Pageable pageable
    );
    @Query("""
    SELECT COALESCE(SUM(o.totalAmount), 0)
    FROM Order o
    WHERE o.status = :status
    """)
    BigDecimal getTotalByStatus(
            @Param("status") OrderStatus status
    );
}
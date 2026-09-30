package com.smartbusiness.businessmanagement.repository;

import com.smartbusiness.businessmanagement.entity.Order;
import com.smartbusiness.businessmanagement.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
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
}
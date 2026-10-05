package com.smartbusiness.businessmanagement.repository;

import com.smartbusiness.businessmanagement.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository
        extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);

    boolean existsByProductId(Long productId);

    List<Inventory> findByQuantityLessThanEqualOrderByQuantityAsc(
            Integer reorderLevel
    );

    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.quantity <= i.reorderLevel
            ORDER BY i.quantity ASC
            """)
    List<Inventory> findLowStock();

    @Query("""
        SELECT COALESCE(SUM(i.quantity), 0)
        FROM Inventory i
        WHERE i.active = true
        """)
    long getCurrentStock();

    @Query("""
        SELECT COUNT(i)
        FROM Inventory i
        WHERE i.active = true
        AND i.quantity <= i.reorderLevel
        """)
    long countLowStock();

    @Query("""
        SELECT i
        FROM Inventory i
        JOIN FETCH i.product p
        WHERE i.active = true
        ORDER BY p.name ASC
        """)
    List<Inventory> findAllActiveWithProduct();

    Page<Inventory> findAllByOrderByIdAsc(
            Pageable pageable
    );

    @Query("""
        SELECT i
        FROM Inventory i
        JOIN i.product p
        JOIN p.category c
        WHERE i.active = true
        AND (
            LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        ORDER BY p.name ASC
        """)
    Page<Inventory> searchInventoryReport(
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
        SELECT i
        FROM Inventory i
        JOIN i.product p
        JOIN p.category c
        WHERE i.active = true
        AND i.quantity <= i.reorderLevel
        AND (
            LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        ORDER BY i.quantity ASC, p.name ASC
        """)
    Page<Inventory> searchLowStockReport(
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
        SELECT i
        FROM Inventory i
        JOIN i.product p
        WHERE i.active = true
        AND i.quantity <= i.reorderLevel
        ORDER BY i.quantity ASC, p.name ASC
        """)
    Page<Inventory> findLowStockReport(
            Pageable pageable
    );
}
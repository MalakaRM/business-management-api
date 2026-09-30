package com.smartbusiness.businessmanagement.repository;

import com.smartbusiness.businessmanagement.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository
        extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);

    boolean existsByProductId(Long productId);

    List<Inventory> findByQuantityLessThanEqualOrderByQuantityAsc(Integer reorderLevel);

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


}
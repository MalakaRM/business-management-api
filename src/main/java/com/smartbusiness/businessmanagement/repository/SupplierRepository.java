package com.smartbusiness.businessmanagement.repository;

import com.smartbusiness.businessmanagement.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    Optional<Supplier> findByName(String name);

    boolean existsByName(String name);
    Page<Supplier> findByActiveTrue(Pageable pageable);
    long countByActiveTrue();
}
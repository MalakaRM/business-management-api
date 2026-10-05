package com.smartbusiness.businessmanagement.repository;

import com.smartbusiness.businessmanagement.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Customer> findByActiveTrue(Pageable pageable);
    long countByActiveTrue();
    Page<Customer> findAllByOrderByIdAsc(Pageable pageable);
}

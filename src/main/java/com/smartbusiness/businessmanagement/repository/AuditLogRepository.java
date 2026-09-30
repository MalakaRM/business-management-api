package com.smartbusiness.businessmanagement.repository;

import com.smartbusiness.businessmanagement.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByUsername(
            String username,
            Pageable pageable
    );

    Page<AuditLog> findByEntityName(
            String entityName,
            Pageable pageable
    );
}
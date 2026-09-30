package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.response.AuditLogResponse;
import com.smartbusiness.businessmanagement.entity.enums.AuditAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditService {

    void log(
            AuditAction action,
            String entityName,
            String entityId,
            String description
    );

    Page<AuditLogResponse> getAll(Pageable pageable);

    Page<AuditLogResponse> getByUsername(
            String username,
            Pageable pageable
    );

    Page<AuditLogResponse> getByEntityName(
            String entityName,
            Pageable pageable
    );
}
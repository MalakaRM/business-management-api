package com.smartbusiness.businessmanagement.dto.response;

import com.smartbusiness.businessmanagement.entity.enums.AuditAction;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        String username,
        AuditAction action,
        String entityName,
        String entityId,
        String description,
        LocalDateTime createdAt
) {
}
package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.response.AuditLogResponse;
import com.smartbusiness.businessmanagement.entity.AuditLog;
import com.smartbusiness.businessmanagement.entity.enums.AuditAction;
import com.smartbusiness.businessmanagement.repository.AuditLogRepository;
import com.smartbusiness.businessmanagement.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public void log(
            AuditAction action,
            String entityName,
            String entityId,
            String description
    ) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String username = authentication.getName();

        AuditLog auditLog = AuditLog.builder()
                .username(username)
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .description(description)
                .build();

        auditLogRepository.save(auditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getAll(Pageable pageable) {

        return auditLogRepository
                .findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getByUsername(
            String username,
            Pageable pageable
    ) {

        return auditLogRepository
                .findByUsername(username, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getByEntityName(
            String entityName,
            Pageable pageable
    ) {

        return auditLogRepository
                .findByEntityName(entityName, pageable)
                .map(this::mapToResponse);
    }

    private AuditLogResponse mapToResponse(AuditLog auditLog) {

        return new AuditLogResponse(
                auditLog.getId(),
                auditLog.getUsername(),
                auditLog.getAction(),
                auditLog.getEntityName(),
                auditLog.getEntityId(),
                auditLog.getDescription(),
                auditLog.getCreatedAt()
        );
    }
}
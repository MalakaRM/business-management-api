package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.AuditLogResponse;
import com.smartbusiness.businessmanagement.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('AUDIT_READ')")
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AuditLogResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Audit logs retrieved successfully",
                        auditService.getAll(pageable)
                )
        );
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ApiResponse<Page<AuditLogResponse>>> getByUsername(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User audit logs retrieved successfully",
                        auditService.getByUsername(
                                username,
                                pageable
                        )
                )
        );
    }

    @GetMapping("/entity/{entityName}")
    public ResponseEntity<ApiResponse<Page<AuditLogResponse>>> getByEntityName(
            @PathVariable String entityName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Entity audit logs retrieved successfully",
                        auditService.getByEntityName(
                                entityName,
                                pageable
                        )
                )
        );
    }
}
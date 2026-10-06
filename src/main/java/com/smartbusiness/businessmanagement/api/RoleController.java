package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.request.CreateRoleRequest;
import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.RoleResponse;
import com.smartbusiness.businessmanagement.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(
            @Valid @RequestBody CreateRoleRequest request
    ) {
        RoleResponse response = roleService.createRole(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Role created successfully",
                        response
                ));
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {

        List<RoleResponse> response = roleService.getAllRoles();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Roles retrieved successfully",
                        response
                )
        );
    }
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @PutMapping("/{roleId}/permissions")
    public ResponseEntity<ApiResponse<RoleResponse>> assignPermissions(
            @PathVariable Long roleId,
            @RequestBody Set<Long> permissionIds
    ) {
        RoleResponse response =
                roleService.assignPermissions(roleId, permissionIds);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Permissions assigned successfully",
                        response
                )
        );
    }
}
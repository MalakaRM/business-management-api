package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.CreateRoleRequest;
import com.smartbusiness.businessmanagement.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {

    RoleResponse createRole(CreateRoleRequest request);

    List<RoleResponse> getAllRoles();
    RoleResponse assignPermissions(
            Long roleId,
            java.util.Set<Long> permissionIds
    );
}
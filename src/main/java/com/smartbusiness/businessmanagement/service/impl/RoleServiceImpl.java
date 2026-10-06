package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.request.CreateRoleRequest;
import com.smartbusiness.businessmanagement.dto.response.RoleResponse;
import com.smartbusiness.businessmanagement.entity.Permission;
import com.smartbusiness.businessmanagement.entity.Role;
import com.smartbusiness.businessmanagement.repository.PermissionRepository;
import com.smartbusiness.businessmanagement.repository.RoleRepository;
import com.smartbusiness.businessmanagement.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public RoleResponse createRole(CreateRoleRequest request) {

        String roleName = request.name()
                .trim()
                .toUpperCase();

        if (roleRepository.existsByName(roleName)) {
            throw new IllegalArgumentException(
                    "Role already exists: " + roleName
            );
        }

        Role role = Role.builder()
                .name(roleName)
                .description(request.description())
                .build();

        Role savedRole = roleRepository.save(role);

        return toResponse(savedRole);
    }

    @Override
    public List<RoleResponse> getAllRoles() {

        return roleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private RoleResponse toResponse(Role role) {

        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getPermissions()
                        .stream()
                        .map(permission -> permission.getName())
                        .collect(java.util.stream.Collectors.toSet())
        );
    }
    @Override
    public RoleResponse assignPermissions(
            Long roleId,
            Set<Long> permissionIds
    ) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Role not found: " + roleId
                        )
                );

        Set<Permission> permissions = permissionIds.stream()
                .map(permissionId ->
                        permissionRepository.findById(permissionId)
                                .orElseThrow(() ->
                                        new IllegalArgumentException(
                                                "Permission not found: " + permissionId
                                        )
                                )
                )
                .collect(Collectors.toSet());

        role.setPermissions(permissions);

        Role savedRole = roleRepository.save(role);

        return toResponse(savedRole);
    }
}
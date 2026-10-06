package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.response.PermissionResponse;
import com.smartbusiness.businessmanagement.entity.Permission;
import com.smartbusiness.businessmanagement.repository.PermissionRepository;
import com.smartbusiness.businessmanagement.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public List<PermissionResponse> getAllPermissions() {

        return permissionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private PermissionResponse toResponse(Permission permission) {

        return new PermissionResponse(
                permission.getId(),
                permission.getName(),
                permission.getDescription()
        );
    }
}
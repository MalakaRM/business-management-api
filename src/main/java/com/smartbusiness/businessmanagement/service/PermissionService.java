package com.smartbusiness.businessmanagement.service;



import com.smartbusiness.businessmanagement.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {

    List<PermissionResponse> getAllPermissions();
}
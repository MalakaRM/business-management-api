package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.RegisterRequest;
import com.smartbusiness.businessmanagement.dto.response.RegisterResponse;

public interface UserService {

    RegisterResponse createUser(RegisterRequest request);

}
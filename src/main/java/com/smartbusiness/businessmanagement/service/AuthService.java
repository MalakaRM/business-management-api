package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.ChangePasswordRequest;
import com.smartbusiness.businessmanagement.dto.request.LoginRequest;
import com.smartbusiness.businessmanagement.dto.request.RegisterRequest;
import com.smartbusiness.businessmanagement.dto.response.ChangePasswordResponse;
import com.smartbusiness.businessmanagement.dto.response.LoginResponse;
import com.smartbusiness.businessmanagement.dto.response.RegisterResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
    RegisterResponse register(RegisterRequest request);
    ChangePasswordResponse changePassword(ChangePasswordRequest request);

}
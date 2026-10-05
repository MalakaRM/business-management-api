package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.request.ChangePasswordRequest;
import com.smartbusiness.businessmanagement.dto.request.LoginRequest;
import com.smartbusiness.businessmanagement.dto.request.RegisterRequest;
import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.ChangePasswordResponse;
import com.smartbusiness.businessmanagement.dto.response.LoginResponse;
import com.smartbusiness.businessmanagement.dto.response.RegisterResponse;
import com.smartbusiness.businessmanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login Successful",
                        response
                )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        RegisterResponse response =
                authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Registration successful",
                                response
                        )
                );
    }
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<ChangePasswordResponse>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        ChangePasswordResponse response =
                authService.changePassword(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Password changed successfully",
                        response
                )
        );
    }
}
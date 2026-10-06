package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.request.RegisterRequest;
import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.RegisterResponse;
import com.smartbusiness.businessmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('USER_MANAGE')")
    @PostMapping
    public ResponseEntity<ApiResponse<RegisterResponse>> createUser(
            @Valid @RequestBody RegisterRequest request
    ) {

        RegisterResponse response = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "User created successfully",
                        response
                ));
    }
}
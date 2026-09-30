package com.smartbusiness.businessmanagement.dto.response;

public record RegisterResponse(
        Long id,
        String username,
        String email
) {
}
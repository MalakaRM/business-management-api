package com.smartbusiness.businessmanagement.dto.response;

public record CustomerResponse(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        boolean active
) {}
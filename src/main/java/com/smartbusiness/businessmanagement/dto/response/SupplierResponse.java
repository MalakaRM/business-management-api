package com.smartbusiness.businessmanagement.dto.response;

public record SupplierResponse(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        boolean active
) {}
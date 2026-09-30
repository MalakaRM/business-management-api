package com.smartbusiness.businessmanagement.dto.response;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        boolean active
) {
}
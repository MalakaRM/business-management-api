package com.smartbusiness.businessmanagement.dto.response;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String sku,
        BigDecimal price,
        BigDecimal costPrice,
        String description,
        boolean active,
        Long categoryId,
        String categoryName
) {
}
package com.smartbusiness.businessmanagement.dto.response;

import java.math.BigDecimal;

public record InventoryResponse(
        Long id,
        Long productId,
        String productName,
        String sku,
        String categoryName,
        Integer quantity,
        Integer reorderLevel,
        BigDecimal price,
        boolean lowStock,
        boolean active
) {
}
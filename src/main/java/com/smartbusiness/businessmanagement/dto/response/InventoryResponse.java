package com.smartbusiness.businessmanagement.dto.response;

public record InventoryResponse(
        Long id,
        Long productId,
        String productName,
        Integer quantity,
        Integer reorderLevel,
        boolean lowStock,
        boolean active
) {
}
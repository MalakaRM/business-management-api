package com.smartbusiness.businessmanagement.dto.response;

import java.math.BigDecimal;

public record InventoryReportItemResponse(
        Long productId,
        String productName,
        String sku,
        Integer quantity,
        Integer reorderLevel,
        BigDecimal stockValue,
        boolean lowStock
) {
}
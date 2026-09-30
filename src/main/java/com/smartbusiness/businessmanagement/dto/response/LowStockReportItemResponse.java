package com.smartbusiness.businessmanagement.dto.response;

public record LowStockReportItemResponse(
        Long productId,
        String productName,
        String sku,
        Integer quantity,
        Integer reorderLevel,
        Integer shortage
) {
}
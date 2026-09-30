package com.smartbusiness.businessmanagement.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record InventoryReportResponse(
        long totalProducts,
        long totalStockQuantity,
        BigDecimal totalInventoryValue,
        List<InventoryReportItemResponse> items
) {
}
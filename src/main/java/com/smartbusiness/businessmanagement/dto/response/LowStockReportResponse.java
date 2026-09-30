package com.smartbusiness.businessmanagement.dto.response;

import java.util.List;

public record LowStockReportResponse(
        long totalLowStockProducts,
        List<LowStockReportItemResponse> items
) {
}
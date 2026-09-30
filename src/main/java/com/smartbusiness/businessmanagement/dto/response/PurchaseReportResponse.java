package com.smartbusiness.businessmanagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PurchaseReportResponse(
        LocalDate from,
        LocalDate to,
        long totalPurchases,
        BigDecimal totalPurchaseAmount
) {
}
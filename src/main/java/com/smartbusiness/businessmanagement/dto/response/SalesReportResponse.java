
package com.smartbusiness.businessmanagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesReportResponse(
        LocalDate from,
        LocalDate to,
        long totalOrders,
        BigDecimal totalSales
) {
}
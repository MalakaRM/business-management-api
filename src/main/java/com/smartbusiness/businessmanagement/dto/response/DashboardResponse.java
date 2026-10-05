package com.smartbusiness.businessmanagement.dto.response;

import java.math.BigDecimal;

public record DashboardResponse(
        Long totalProducts,
        Long totalCustomers,
        Long totalSuppliers,
        Long currentStock,
        Long lowStockCount,
        Long pendingOrders,
        BigDecimal todaySales,
        BigDecimal revenue
) {
}
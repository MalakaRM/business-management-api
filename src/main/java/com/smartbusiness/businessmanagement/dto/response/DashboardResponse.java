package com.smartbusiness.businessmanagement.dto.response;

import java.math.BigDecimal;

public record DashboardResponse(
        long totalProducts,
        long totalCustomers,
        long totalSuppliers,
        long currentStock,
        long lowStockCount,
        long pendingOrders,
        BigDecimal todaySales,
        BigDecimal todayPurchases,
        BigDecimal revenue
) {
}
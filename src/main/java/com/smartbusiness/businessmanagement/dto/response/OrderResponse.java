package com.smartbusiness.businessmanagement.dto.response;

import com.smartbusiness.businessmanagement.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        Long customerId,
        String customerName,
        LocalDate orderDate,
        OrderStatus status,
        BigDecimal totalAmount,
        List<OrderItemResponse> items
) {}
package com.smartbusiness.businessmanagement.dto.response;

import com.smartbusiness.businessmanagement.entity.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record InvoiceResponse(
        Long id,
        String invoiceNumber,
        Long orderId,
        String orderNumber,
        LocalDateTime issuedAt,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        BigDecimal amountTendered,
        BigDecimal changeAmount,
        List<OrderItemResponse> items
) {
}
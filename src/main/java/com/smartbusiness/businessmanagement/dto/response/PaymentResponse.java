package com.smartbusiness.businessmanagement.dto.response;

import com.smartbusiness.businessmanagement.entity.enums.PaymentMethod;
import com.smartbusiness.businessmanagement.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        String orderNumber,
        BigDecimal amount,
        PaymentMethod method,
        PaymentStatus status,
        BigDecimal amountTendered,
        BigDecimal changeAmount,
        LocalDateTime paidAt
) {
}
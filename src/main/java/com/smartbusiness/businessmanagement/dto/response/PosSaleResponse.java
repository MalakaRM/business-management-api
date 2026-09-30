package com.smartbusiness.businessmanagement.dto.response;

import com.smartbusiness.businessmanagement.entity.enums.OrderStatus;
import com.smartbusiness.businessmanagement.entity.enums.PaymentMethod;
import com.smartbusiness.businessmanagement.entity.enums.PaymentStatus;

import java.math.BigDecimal;

public record PosSaleResponse(
        Long orderId,
        String orderNumber,
        OrderStatus orderStatus,
        Long paymentId,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        BigDecimal totalAmount,
        BigDecimal amountTendered,
        BigDecimal changeAmount
) {
}
package com.smartbusiness.businessmanagement.dto.request;

import com.smartbusiness.businessmanagement.entity.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentCreateRequest(

        @NotNull
        Long orderId,

        @NotNull
        PaymentMethod method,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal amountTendered
) {
}
package com.smartbusiness.businessmanagement.dto.request;

import com.smartbusiness.businessmanagement.entity.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PosSaleRequest(

        @NotNull
        Long orderId,

        @NotNull
        PaymentMethod paymentMethod,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal amountTendered
) {
}
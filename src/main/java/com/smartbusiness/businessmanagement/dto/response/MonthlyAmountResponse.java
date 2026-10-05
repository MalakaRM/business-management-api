package com.smartbusiness.businessmanagement.dto.response;

import java.math.BigDecimal;

public record MonthlyAmountResponse(
        String month,
        BigDecimal amount
) {
}
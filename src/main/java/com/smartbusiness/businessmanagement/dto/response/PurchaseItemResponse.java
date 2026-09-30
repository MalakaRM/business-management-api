package com.smartbusiness.businessmanagement.dto.response;

import java.math.BigDecimal;

public record PurchaseItemResponse(
        Long id,
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitCost,
        BigDecimal subtotal
) {}
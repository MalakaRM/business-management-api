package com.smartbusiness.businessmanagement.dto.response;

import com.smartbusiness.businessmanagement.entity.enums.StockMovementType;


import java.time.LocalDateTime;

public record StockMovementResponse(
        Long id,
        Long productId,
        String productName,
        StockMovementType type,
        Integer quantity,
        Integer quantityBefore,
        Integer quantityAfter,
        String reason,
        LocalDateTime createdAt
) {
}
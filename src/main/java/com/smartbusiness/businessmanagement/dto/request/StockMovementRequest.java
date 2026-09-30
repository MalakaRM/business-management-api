package com.smartbusiness.businessmanagement.dto.request;


import com.smartbusiness.businessmanagement.entity.enums.StockMovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StockMovementRequest(

        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Movement type is required")
        StockMovementType type,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,

        @Size(
                max = 255,
                message = "Reason cannot exceed 255 characters"
        )
        String reason
) {
}
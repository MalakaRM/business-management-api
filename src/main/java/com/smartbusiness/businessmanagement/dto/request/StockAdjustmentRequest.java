package com.smartbusiness.businessmanagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StockAdjustmentRequest(

        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "New quantity is required")
        @Min(value = 0, message = "New quantity cannot be negative")
        Integer newQuantity,

        @NotNull(message = "Reason is required")
        @Size(
                min = 3,
                max = 255,
                message = "Reason must be between 3 and 255 characters"
        )
        String reason
) {}
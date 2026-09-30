package com.smartbusiness.businessmanagement.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateRequest(

        @NotBlank(message = "Product name is required")
        @Size(max = 150, message = "Product name cannot exceed 150 characters")
        String name,

        @NotBlank(message = "SKU is required")
        @Size(max = 100, message = "SKU cannot exceed 100 characters")
        String sku,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false,
                message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Cost price is required")
        @DecimalMin(value = "0.0", inclusive = false,
                message = "Cost price must be greater than 0")
        BigDecimal costPrice,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        @NotNull(message = "Category ID is required")
        Long categoryId
) {
}
package com.smartbusiness.businessmanagement.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record OrderCreateRequest(

        @Size(
                max = 100,
                message = "Order number cannot exceed 100 characters"
        )
        String orderNumber,

        Long customerId,

        @NotNull(message = "Order date is required")
        LocalDate orderDate,

        @NotEmpty(
                message = "At least one order item is required"
        )
        @Valid
        List<OrderItemCreateRequest> items
) {}
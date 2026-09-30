package com.smartbusiness.businessmanagement.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record PurchaseCreateRequest(

        @NotBlank(message = "Reference number is required")
        @Size(
                max = 100,
                message = "Reference number cannot exceed 100 characters"
        )
        String referenceNumber,

        @NotNull(message = "Supplier ID is required")
        Long supplierId,

        @NotNull(message = "Purchase date is required")
        LocalDate purchaseDate,

        @Size(
                max = 500,
                message = "Notes cannot exceed 500 characters"
        )
        String notes,

        @NotEmpty(message = "At least one purchase item is required")
        @Valid
        List<PurchaseItemRequest> items
) {}
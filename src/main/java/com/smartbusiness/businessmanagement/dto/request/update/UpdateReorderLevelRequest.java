package com.smartbusiness.businessmanagement.dto.request.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateReorderLevelRequest(

        @NotNull(message = "Reorder level is required")
        @Min(value = 0, message = "Reorder level cannot be negative")
        Integer reorderLevel
) {}
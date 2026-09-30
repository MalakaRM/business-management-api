package com.smartbusiness.businessmanagement.dto.response;



import com.smartbusiness.businessmanagement.entity.enums.PurchaseStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PurchaseResponse(
        Long id,
        String referenceNumber,
        Long supplierId,
        String supplierName,
        LocalDate purchaseDate,
        PurchaseStatus status,
        BigDecimal totalAmount,
        String notes,
        List<PurchaseItemResponse> items
) {}
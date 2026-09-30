package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.response.PurchaseReportResponse;
import com.smartbusiness.businessmanagement.entity.enums.PurchaseStatus;
import com.smartbusiness.businessmanagement.repository.PurchaseRepository;
import com.smartbusiness.businessmanagement.service.PurchaseReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PurchaseReportServiceImpl implements PurchaseReportService {

    private final PurchaseRepository purchaseRepository;

    @Override
    @Transactional(readOnly = true)
    public PurchaseReportResponse getPurchaseReport(
            LocalDate from,
            LocalDate to
    ) {

        if (from == null || to == null) {
            throw new IllegalArgumentException(
                    "From date and to date are required"
            );
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "From date cannot be after to date"
            );
        }

        long totalPurchases =
                purchaseRepository.countByStatusAndDateBetween(
                        PurchaseStatus.RECEIVED,
                        from,
                        to
                );

        BigDecimal totalPurchaseAmount =
                purchaseRepository.getTotalByStatusAndDateBetween(
                        PurchaseStatus.RECEIVED,
                        from,
                        to
                );

        return new PurchaseReportResponse(
                from,
                to,
                totalPurchases,
                totalPurchaseAmount
        );
    }
}
package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.response.SalesReportResponse;

import com.smartbusiness.businessmanagement.entity.enums.OrderStatus;
import com.smartbusiness.businessmanagement.repository.OrderRepository;
import com.smartbusiness.businessmanagement.service.SalesReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SalesReportServiceImpl implements SalesReportService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public SalesReportResponse getSalesReport(
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

        long totalOrders =
                orderRepository.countByStatusAndDateBetween(
                        OrderStatus.CONFIRMED,
                        from,
                        to
                );

        BigDecimal totalSales =
                orderRepository.getTotalByStatusAndDateBetween(
                        OrderStatus.CONFIRMED,
                        from,
                        to
                );

        return new SalesReportResponse(
                from,
                to,
                totalOrders,
                totalSales
        );
    }
}
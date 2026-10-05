package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.response.MonthlyAmountResponse;
import com.smartbusiness.businessmanagement.dto.response.PurchaseReportResponse;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseReportService {

    PurchaseReportResponse getPurchaseReport(
            LocalDate from,
            LocalDate to
    );
    List<MonthlyAmountResponse> getMonthlyPurchases(
            LocalDate from,
            LocalDate to
    );
}
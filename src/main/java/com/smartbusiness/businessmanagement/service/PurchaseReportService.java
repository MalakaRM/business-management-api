package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.response.PurchaseReportResponse;

import java.time.LocalDate;

public interface PurchaseReportService {

    PurchaseReportResponse getPurchaseReport(
            LocalDate from,
            LocalDate to
    );
}
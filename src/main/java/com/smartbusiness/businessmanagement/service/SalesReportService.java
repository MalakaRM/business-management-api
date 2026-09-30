package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.response.SalesReportResponse;

import java.time.LocalDate;

public interface SalesReportService {

    SalesReportResponse getSalesReport(
            LocalDate from,
            LocalDate to
    );
}
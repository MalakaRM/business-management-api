package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.response.MonthlyAmountResponse;
import com.smartbusiness.businessmanagement.dto.response.SalesReportResponse;

import java.time.LocalDate;
import java.util.List;

public interface SalesReportService {

    SalesReportResponse getSalesReport(
            LocalDate from,
            LocalDate to
    );
    List<MonthlyAmountResponse> getMonthlySales(
            LocalDate from,
            LocalDate to
    );
}
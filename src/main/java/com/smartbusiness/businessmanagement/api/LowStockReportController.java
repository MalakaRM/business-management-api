package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.LowStockReportResponse;
import com.smartbusiness.businessmanagement.repository.LowStockReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports/low-stock")
@RequiredArgsConstructor
public class LowStockReportController {

    private final LowStockReportService lowStockReportService;

    @PreAuthorize("hasAuthority('REPORT_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<LowStockReportResponse>> getLowStockReport() {

        LowStockReportResponse response =
                lowStockReportService.getLowStockReport();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Low stock report retrieved successfully",
                        response
                )
        );
    }
}
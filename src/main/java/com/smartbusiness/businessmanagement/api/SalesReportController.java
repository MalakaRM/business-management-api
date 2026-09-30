package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.SalesReportResponse;
import com.smartbusiness.businessmanagement.service.SalesReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports/sales")
@RequiredArgsConstructor
public class SalesReportController {

    private final SalesReportService salesReportService;

    @PreAuthorize("hasAuthority('REPORT_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<SalesReportResponse>> getSalesReport(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {

        SalesReportResponse response =
                salesReportService.getSalesReport(from, to);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Sales report retrieved successfully",
                        response
                )
        );
    }
}
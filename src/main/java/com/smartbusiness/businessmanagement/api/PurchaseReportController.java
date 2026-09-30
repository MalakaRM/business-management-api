package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.PurchaseReportResponse;
import com.smartbusiness.businessmanagement.service.PurchaseReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports/purchases")
@RequiredArgsConstructor
public class PurchaseReportController {

    private final PurchaseReportService purchaseReportService;

    @PreAuthorize("hasAuthority('REPORT_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<PurchaseReportResponse>> getPurchaseReport(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {

        PurchaseReportResponse response =
                purchaseReportService.getPurchaseReport(from, to);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Purchase report retrieved successfully",
                        response
                )
        );
    }
}
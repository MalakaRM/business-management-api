package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.InventoryReportResponse;
import com.smartbusiness.businessmanagement.service.InventoryReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports/inventory")
@RequiredArgsConstructor
public class InventoryReportController {

    private final InventoryReportService inventoryReportService;

    @PreAuthorize("hasAuthority('REPORT_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<InventoryReportResponse>> getInventoryReport() {

        InventoryReportResponse response =
                inventoryReportService.getInventoryReport();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Inventory report retrieved successfully",
                        response
                )
        );
    }
}
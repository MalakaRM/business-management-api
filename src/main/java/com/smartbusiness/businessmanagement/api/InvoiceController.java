package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.InvoiceResponse;
import com.smartbusiness.businessmanagement.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    @PostMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> createInvoice(
            @PathVariable Long orderId
    ) {

        InvoiceResponse response =
                invoiceService.createInvoice(orderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Invoice created successfully",
                        response
                )
        );
    }

    @PreAuthorize("hasAuthority('ORDER_READ')")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoice(
            @PathVariable Long orderId
    ) {

        InvoiceResponse response =
                invoiceService.getInvoiceByOrderId(orderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Invoice retrieved successfully",
                        response
                )
        );
    }
}
package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.request.PaymentCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.PaymentResponse;
import com.smartbusiness.businessmanagement.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @Valid @RequestBody PaymentCreateRequest request
    ) {

        PaymentResponse response =
                paymentService.createPayment(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Payment created successfully",
                        response
                )
        );
    }

    @PreAuthorize("hasAuthority('ORDER_READ')")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @PathVariable Long orderId
    ) {

        PaymentResponse response =
                paymentService.getPaymentByOrderId(orderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Payment retrieved successfully",
                        response
                )
        );
    }
}
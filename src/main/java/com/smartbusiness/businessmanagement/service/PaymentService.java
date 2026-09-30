package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.PaymentCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(
            PaymentCreateRequest request
    );

    PaymentResponse getPaymentByOrderId(Long orderId);
}
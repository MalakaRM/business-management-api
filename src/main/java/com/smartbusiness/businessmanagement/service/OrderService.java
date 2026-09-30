package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.OrderCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponse createOrder(
            OrderCreateRequest request
    );

    OrderResponse confirmOrder(
            Long id
    );
    Page<OrderResponse> getAllOrders(
            Pageable pageable
    );

    OrderResponse getOrderById(
            Long id
    );
    void cancelOrder(Long id);
}
package com.smartbusiness.businessmanagement.mapper;

import com.smartbusiness.businessmanagement.dto.request.OrderCreateRequest;
import com.smartbusiness.businessmanagement.dto.request.OrderItemCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.OrderItemResponse;
import com.smartbusiness.businessmanagement.dto.response.OrderResponse;
import com.smartbusiness.businessmanagement.entity.Customer;
import com.smartbusiness.businessmanagement.entity.Order;
import com.smartbusiness.businessmanagement.entity.OrderItem;
import com.smartbusiness.businessmanagement.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {

    public Order toEntity(
            OrderCreateRequest request,
            Customer customer,
            String orderNumber
    ) {
        return Order.builder()
                .orderNumber(orderNumber)
                .customer(customer)
                .orderDate(request.orderDate())
                .build();
    }

    public OrderItem toItemEntity(
            OrderItemCreateRequest request,
            Product product,
            Order order
    ) {
        BigDecimal unitPrice = product.getPrice();

        BigDecimal subtotal = unitPrice.multiply(
                BigDecimal.valueOf(request.quantity())
        );

        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(request.quantity())
                .unitPrice(unitPrice)
                .subtotal(subtotal)
                .build();
    }

    public OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items =
                order.getItems()
                        .stream()
                        .map(this::toItemResponse)
                        .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomer() != null
                        ? order.getCustomer().getId()
                        : null,
                order.getCustomer() != null
                        ? order.getCustomer().getName()
                        : null,
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount(),
                items
        );
    }

    private OrderItemResponse toItemResponse(
            OrderItem item
    ) {
        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }
}
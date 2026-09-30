package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.request.PaymentCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.PaymentResponse;
import com.smartbusiness.businessmanagement.entity.Order;
import com.smartbusiness.businessmanagement.entity.Payment;
import com.smartbusiness.businessmanagement.entity.enums.OrderStatus;
import com.smartbusiness.businessmanagement.entity.enums.PaymentMethod;
import com.smartbusiness.businessmanagement.entity.enums.PaymentStatus;
import com.smartbusiness.businessmanagement.exception.ResourceAlreadyExistsException;
import com.smartbusiness.businessmanagement.exception.ResourceNotFoundException;
import com.smartbusiness.businessmanagement.repository.OrderRepository;
import com.smartbusiness.businessmanagement.repository.PaymentRepository;
import com.smartbusiness.businessmanagement.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(
            PaymentCreateRequest request
    ) {

        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found: " + request.orderId()
                        )
                );

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Payment can only be created for PENDING orders"
            );
        }

        if (paymentRepository.existsByOrderId(order.getId())) {
            throw new ResourceAlreadyExistsException(
                    "Payment already exists for this order"
            );
        }

        BigDecimal totalAmount = order.getTotalAmount();
        BigDecimal amountTendered = request.amountTendered();

        if (request.method() == PaymentMethod.CASH) {

            if (amountTendered.compareTo(totalAmount) < 0) {
                throw new IllegalArgumentException(
                        "Insufficient cash amount"
                );
            }

        } else if (request.method() == PaymentMethod.CARD) {

            amountTendered = totalAmount;
        }

        BigDecimal changeAmount =
                amountTendered.subtract(totalAmount);

        Payment payment = Payment.builder()
                .order(order)
                .amount(totalAmount)
                .method(request.method())
                .status(PaymentStatus.PAID)
                .amountTendered(amountTendered)
                .changeAmount(changeAmount)
                .paidAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(Long orderId) {

        Payment payment = paymentRepository
                .findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for order: " + orderId
                        )
                );

        return mapToResponse(payment);
    }

    private PaymentResponse mapToResponse(Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getOrder().getOrderNumber(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getAmountTendered(),
                payment.getChangeAmount(),
                payment.getPaidAt()
        );
    }
}
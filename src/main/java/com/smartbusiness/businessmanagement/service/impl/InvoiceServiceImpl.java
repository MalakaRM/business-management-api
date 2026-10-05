package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.response.InvoiceResponse;
import com.smartbusiness.businessmanagement.dto.response.OrderItemResponse;
import com.smartbusiness.businessmanagement.entity.Invoice;
import com.smartbusiness.businessmanagement.entity.Order;
import com.smartbusiness.businessmanagement.entity.Payment;
import com.smartbusiness.businessmanagement.entity.enums.OrderStatus;
import com.smartbusiness.businessmanagement.exception.ResourceAlreadyExistsException;
import com.smartbusiness.businessmanagement.exception.ResourceNotFoundException;
import com.smartbusiness.businessmanagement.mapper.OrderMapper;
import com.smartbusiness.businessmanagement.repository.InvoiceRepository;
import com.smartbusiness.businessmanagement.repository.OrderRepository;
import com.smartbusiness.businessmanagement.repository.PaymentRepository;
import com.smartbusiness.businessmanagement.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl
        implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public InvoiceResponse createInvoice(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found: " + orderId
                        )
                );

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException(
                    "Invoice can only be created for confirmed orders"
            );
        }

        if (invoiceRepository.existsByOrderId(orderId)) {
            throw new ResourceAlreadyExistsException(
                    "Invoice already exists for this order"
            );
        }

        Payment payment = paymentRepository
                .findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for order: " + orderId
                        )
                );

        String invoiceNumber =
                "INV-" + UUID.randomUUID();

        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .order(order)
                .issuedAt(LocalDateTime.now())
                .build();

        Invoice saved = invoiceRepository.save(invoice);

        return mapToResponse(saved, payment);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceByOrderId(Long orderId) {

        Invoice invoice = invoiceRepository
                .findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invoice not found for order: " + orderId
                        )
                );

        Payment payment = paymentRepository
                .findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for order: " + orderId
                        )
                );

        return mapToResponse(invoice, payment);
    }

    private InvoiceResponse mapToResponse(Invoice invoice, Payment payment) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String cashierName = authentication.getName();

        Order order = invoice.getOrder();

        List<OrderItemResponse> items =
                order.getItems()
                        .stream()
                        .map(item -> new OrderItemResponse(
                                item.getId(),
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getSubtotal()
                        ))
                        .toList();

        return new InvoiceResponse(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                order.getId(),
                order.getOrderNumber(),
                invoice.getIssuedAt(),
                cashierName,
                order.getTotalAmount(),
                payment.getMethod(),
                payment.getAmountTendered(),
                payment.getChangeAmount(),
                items
        );
    }


}
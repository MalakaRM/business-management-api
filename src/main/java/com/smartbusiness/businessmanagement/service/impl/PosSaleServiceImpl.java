package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.request.PosSaleRequest;
import com.smartbusiness.businessmanagement.dto.response.PosSaleResponse;
import com.smartbusiness.businessmanagement.entity.Order;
import com.smartbusiness.businessmanagement.entity.OrderItem;
import com.smartbusiness.businessmanagement.entity.Payment;
import com.smartbusiness.businessmanagement.entity.enums.AuditAction;
import com.smartbusiness.businessmanagement.entity.enums.OrderStatus;
import com.smartbusiness.businessmanagement.entity.enums.PaymentMethod;
import com.smartbusiness.businessmanagement.entity.enums.PaymentStatus;
import com.smartbusiness.businessmanagement.exception.ResourceAlreadyExistsException;
import com.smartbusiness.businessmanagement.exception.ResourceNotFoundException;
import com.smartbusiness.businessmanagement.repository.OrderRepository;
import com.smartbusiness.businessmanagement.repository.PaymentRepository;
import com.smartbusiness.businessmanagement.service.AuditService;
import com.smartbusiness.businessmanagement.service.InventoryService;
import com.smartbusiness.businessmanagement.service.InvoiceService;
import com.smartbusiness.businessmanagement.service.PosSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PosSaleServiceImpl implements PosSaleService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final InventoryService inventoryService;
    private final InvoiceService invoiceService;
    private final AuditService auditService;

    @Override
    @Transactional
    public PosSaleResponse completeSale(
            PosSaleRequest request
    ) {

        // 1. Find order
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found: "
                                        + request.orderId()
                        )
                );

        // 2. Order must be PENDING
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only PENDING orders can be completed"
            );
        }

        // 3. Prevent duplicate payment
        if (paymentRepository.existsByOrderId(order.getId())) {
            throw new ResourceAlreadyExistsException(
                    "Payment already exists for this order"
            );
        }

        BigDecimal totalAmount =
                order.getTotalAmount();

        BigDecimal amountTendered =
                request.amountTendered();

        // 4. Validate payment
        if (request.paymentMethod() == PaymentMethod.CASH) {

            if (amountTendered.compareTo(totalAmount) < 0) {
                throw new IllegalArgumentException(
                        "Insufficient cash amount"
                );
            }

        } else if (request.paymentMethod() == PaymentMethod.CARD) {

            amountTendered = totalAmount;
        }

        BigDecimal changeAmount =
                amountTendered.subtract(totalAmount);

        // 5. Validate ALL stock BEFORE modifying anything
        for (OrderItem item : order.getItems()) {

            inventoryService.validateStock(
                    item.getProduct(),
                    item.getQuantity()
            );
        }

        // 6. Deduct stock + create SALE movements
        for (OrderItem item : order.getItems()) {

            inventoryService.deductStock(
                    item.getProduct(),
                    item.getQuantity(),
                    "POS Sale: " + order.getOrderNumber()
            );
        }

        // 7. Create payment
        Payment payment = Payment.builder()
                .order(order)
                .amount(totalAmount)
                .method(request.paymentMethod())
                .status(PaymentStatus.PAID)
                .amountTendered(amountTendered)
                .changeAmount(changeAmount)
                .paidAt(LocalDateTime.now())
                .build();

        Payment savedPayment =
                paymentRepository.save(payment);

        // 8. Confirm order
        order.setStatus(OrderStatus.CONFIRMED);

        // 8. Confirm order
        order.setStatus(OrderStatus.CONFIRMED);

        Order savedOrder =
                orderRepository.save(order);

        // 9. Create invoice
        invoiceService.createInvoice(savedOrder.getId());

        auditService.log(
                AuditAction.SALE,
                "Order",//pos sale ekt wenm enity ekk hdl ne eki order methn
                savedOrder.getId().toString(),
                "Completed POS sale: "
                        + savedOrder.getOrderNumber()
        );

        // 10. Return POS result
        return new PosSaleResponse(
                savedOrder.getId(),
                savedOrder.getOrderNumber(),
                savedOrder.getStatus(),
                savedPayment.getId(),
                savedPayment.getMethod(),
                savedPayment.getStatus(),
                savedPayment.getAmount(),
                savedPayment.getAmountTendered(),
                savedPayment.getChangeAmount()
        );
    }
}
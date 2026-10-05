package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.request.OrderCreateRequest;
import com.smartbusiness.businessmanagement.dto.request.OrderItemCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.OrderResponse;
import com.smartbusiness.businessmanagement.entity.*;
import com.smartbusiness.businessmanagement.entity.enums.OrderStatus;
import com.smartbusiness.businessmanagement.entity.enums.StockMovementType;
import com.smartbusiness.businessmanagement.exception.InsufficientStockException;
import com.smartbusiness.businessmanagement.exception.ResourceAlreadyExistsException;
import com.smartbusiness.businessmanagement.exception.ResourceNotFoundException;
import com.smartbusiness.businessmanagement.mapper.OrderMapper;
import com.smartbusiness.businessmanagement.repository.*;
import com.smartbusiness.businessmanagement.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final InventoryRepository inventoryRepository;
    private final StockMovementRepository stockMovementRepository;


    @Override
    @Transactional
    public OrderResponse createOrder(
            OrderCreateRequest request
    ) {

        /*
         * 1. Resolve customer.
         *
         * customerId is optional because
         * walk-in customers are allowed.
         */
        Customer customer = null;

        if (request.customerId() != null) {

            customer = customerRepository
                    .findById(request.customerId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Customer not found with id: "
                                            + request.customerId()
                            )
                    );

            if (!customer.isActive()) {
                throw new IllegalArgumentException(
                        "Cannot create order for inactive customer"
                );
            }
        }

        /*
         * 2. Generate order number if
         * client did not provide one.
         */
        String orderNumber = request.orderNumber();

        if (orderNumber == null
                || orderNumber.isBlank()) {

            orderNumber =
                    "ORD-" + UUID.randomUUID();
        }

        /*
         * 3. Check duplicate order number.
         */
        if (orderRepository.existsByOrderNumber(
                orderNumber
        )) {
            throw new ResourceAlreadyExistsException(
                    "Order number is already registered"
            );
        }

        /*
         * 4. Create Order entity.
         */
        Order order = orderMapper.toEntity(
                request,
                customer,
                orderNumber
        );

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        /*
         * 5. Resolve products and create
         * OrderItems.
         */
        for (OrderItemCreateRequest itemRequest :
                request.items()) {

            Product product =
                    productRepository.findById(
                            itemRequest.productId()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found with id: "
                                            + itemRequest.productId()
                            )
                    );

            if (!product.isActive()) {
                throw new IllegalArgumentException(
                        "Cannot order inactive product: "
                                + product.getName()
                );
            }

            OrderItem orderItem =
                    orderMapper.toItemEntity(
                            itemRequest,
                            product,
                            order
                    );

            order.getItems().add(orderItem);

            totalAmount =
                    totalAmount.add(
                            orderItem.getSubtotal()
                    );
        }

        /*
         * 6. Backend calculates total.
         */
        order.setTotalAmount(totalAmount);

        /*
         * Status remains PENDING.
         */
        Order savedOrder =
                orderRepository.save(order);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse confirmOrder(Long id) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: "
                                                + id
                                )
                        );

        // Only PENDING orders can be confirmed
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only PENDING orders can be confirmed"
            );
        }

        /*
         * Step 1:
         * Check stock for ALL order items first.
         *
         * No stock is changed during this phase.
         */
        for (OrderItem item : order.getItems()) {

            Inventory inventory =
                    inventoryRepository
                            .findByProductId(
                                    item.getProduct().getId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Inventory not found for product: "
                                                    + item.getProduct().getName()
                                    )
                            );

            if (inventory.getQuantity()
                    < item.getQuantity()) {

                throw new InsufficientStockException(
                        "Insufficient stock for product: "
                                + item.getProduct().getName()
                                + ". Available: "
                                + inventory.getQuantity()
                                + ", requested: "
                                + item.getQuantity()
                );
            }
        }

        /*
         * Step 2:
         * All stock checks passed.
         *
         * Now update inventory and create
         * SALE stock movements.
         */
        for (OrderItem item : order.getItems()) {

            Inventory inventory =
                    inventoryRepository
                            .findByProductId(
                                    item.getProduct().getId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Inventory not found for product: "
                                                    + item.getProduct().getName()
                                    )
                            );

            int quantityBefore =
                    inventory.getQuantity();

            int quantityAfter =
                    quantityBefore - item.getQuantity();

            inventory.setQuantity(quantityAfter);

            inventoryRepository.save(inventory);

            StockMovement movement =
                    StockMovement.builder()
                            .product(item.getProduct())
                            .type(StockMovementType.SALE)
                            .quantity(item.getQuantity())
                            .quantityBefore(quantityBefore)
                            .quantityAfter(quantityAfter)
                            .reason(
                                    "Order confirmed: "
                                            + order.getOrderNumber()
                            )
                            .build();

            stockMovementRepository.save(movement);
        }

        /*
         * Step 3:
         * Everything succeeded.
         */
        order.setStatus(OrderStatus.CONFIRMED);

        Order savedOrder =
                orderRepository.save(order);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(
            Pageable pageable
    ) {
        return orderRepository
                .findAll(pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {

        Order order =
                orderRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: "
                                                + id
                                )
                        );

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {

        Order order =
                orderRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: "
                                                + id
                                )
                        );

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Order is already cancelled"
            );
        }

        /*
         * PENDING order:
         * No stock was deducted.
         * Therefore simply cancel it.
         */
        if (order.getStatus() == OrderStatus.PENDING) {

            order.setStatus(OrderStatus.CANCELLED);

            orderRepository.save(order);

            return;
        }

        /*
         * CONFIRMED order:
         * Stock was already deducted.
         * Therefore restore stock.
         */
        if (order.getStatus() == OrderStatus.CONFIRMED) {

            for (OrderItem item : order.getItems()) {

                Inventory inventory =
                        inventoryRepository
                                .findByProductId(
                                        item.getProduct().getId()
                                )
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "Inventory not found for product: "
                                                        + item.getProduct()
                                                        .getName()
                                        )
                                );

                int quantityBefore =
                        inventory.getQuantity();

                int quantityAfter =
                        quantityBefore + item.getQuantity();

                inventory.setQuantity(quantityAfter);

                inventoryRepository.save(inventory);

                StockMovement movement =
                        StockMovement.builder()
                                .product(item.getProduct())
                                .type(
                                        StockMovementType.ADJUSTMENT
                                )
                                .quantity(item.getQuantity())
                                .quantityBefore(
                                        quantityBefore
                                )
                                .quantityAfter(
                                        quantityAfter
                                )
                                .reason(
                                        "Order cancelled: "
                                                + order.getOrderNumber()
                                )
                                .build();

                stockMovementRepository.save(movement);
            }

            order.setStatus(OrderStatus.CANCELLED);

            orderRepository.save(order);
        }
    }
    @Override
    @Transactional
    public OrderResponse updateOrder(
            Long id,
            OrderCreateRequest request
    ) {

        /*
         * 1. Find existing order.
         */
        Order order =
                orderRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: "
                                                + id
                                )
                        );

        /*
         * 2. Only PENDING orders can be updated.
         *
         * CONFIRMED orders already affected inventory.
         * CANCELLED orders are already closed.
         */
        if (order.getStatus() != OrderStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Only PENDING orders can be updated"
            );
        }

        /*
         * 3. Resolve customer.
         *
         * Customer is optional because
         * walk-in customers are allowed.
         */
        Customer customer = null;

        if (request.customerId() != null) {

            customer =
                    customerRepository
                            .findById(request.customerId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Customer not found with id: "
                                                    + request.customerId()
                                    )
                            );

            if (!customer.isActive()) {

                throw new IllegalArgumentException(
                        "Cannot update order with inactive customer"
                );
            }
        }

        /*
         * 4. Resolve order number.
         *
         * If client does not provide one,
         * keep the existing order number.
         */
        String orderNumber =
                request.orderNumber();

        if (orderNumber == null
                || orderNumber.isBlank()) {

            orderNumber =
                    order.getOrderNumber();
        }

        /*
         * 5. Check duplicate order number.
         *
         * Only reject if the new number belongs
         * to another order.
         */
        if (!orderNumber.equals(
                order.getOrderNumber()
        )
                && orderRepository.existsByOrderNumber(
                orderNumber
        )) {

            throw new ResourceAlreadyExistsException(
                    "Order number is already registered"
            );
        }

        /*
         * 6. Update basic order information.
         */
        order.setOrderNumber(orderNumber);
        order.setCustomer(customer);
        order.setOrderDate(request.orderDate());

        /*
         * 7. Replace existing order items.
         *
         * orphanRemoval = true in Order entity
         * safely removes old OrderItem records.
         */
        order.getItems().clear();

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        /*
         * 8. Resolve products and create
         * new OrderItems.
         */
        for (OrderItemCreateRequest itemRequest :
                request.items()) {

            Product product =
                    productRepository
                            .findById(
                                    itemRequest.productId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Product not found with id: "
                                                    + itemRequest.productId()
                                    )
                            );

            if (!product.isActive()) {

                throw new IllegalArgumentException(
                        "Cannot order inactive product: "
                                + product.getName()
                );
            }

            OrderItem orderItem =
                    orderMapper.toItemEntity(
                            itemRequest,
                            product,
                            order
                    );

            order.getItems().add(orderItem);

            totalAmount =
                    totalAmount.add(
                            orderItem.getSubtotal()
                    );
        }

        /*
         * 9. Backend calculates total.
         */
        order.setTotalAmount(totalAmount);

        /*
         * Status remains PENDING.
         *
         * Inventory is NOT changed here.
         */
        Order savedOrder =
                orderRepository.save(order);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> searchSalesReport(
            LocalDate from,
            LocalDate to,
            String search,
            Pageable pageable
    ) {

        String cleanSearch =
                search == null ? "" : search.trim();

        Page<Order> orders;

        /*
         * Case 1:
         * Date range + search
         */
        if (from != null
                && to != null
                && !cleanSearch.isEmpty()) {

            orders =
                    orderRepository.searchSalesReportByDateAndSearch(
                            from,
                            to,
                            cleanSearch,
                            pageable
                    );
        }

        /*
         * Case 2:
         * Date range only
         */
        else if (from != null
                && to != null) {

            orders =
                    orderRepository.searchSalesReportByDate(
                            from,
                            to,
                            pageable
                    );
        }

        /*
         * Case 3:
         * Search only
         */
        else if (!cleanSearch.isEmpty()) {

            orders =
                    orderRepository.searchSalesReportBySearch(
                            cleanSearch,
                            pageable
                    );
        }

        /*
         * Case 4:
         * No filters
         */
        else {

            orders =
                    orderRepository.findAll(pageable);
        }

        return orders.map(orderMapper::toResponse);
    }

}
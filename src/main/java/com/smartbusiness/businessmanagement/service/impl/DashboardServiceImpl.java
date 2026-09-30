package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.response.DashboardResponse;
import com.smartbusiness.businessmanagement.entity.enums.OrderStatus;
import com.smartbusiness.businessmanagement.entity.enums.PurchaseStatus;
import com.smartbusiness.businessmanagement.repository.CustomerRepository;
import com.smartbusiness.businessmanagement.repository.InventoryRepository;
import com.smartbusiness.businessmanagement.repository.OrderRepository;
import com.smartbusiness.businessmanagement.repository.ProductRepository;
import com.smartbusiness.businessmanagement.repository.PurchaseRepository;
import com.smartbusiness.businessmanagement.repository.SupplierRepository;
import com.smartbusiness.businessmanagement.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;
    private final PurchaseRepository purchaseRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {

        LocalDate today = LocalDate.now();

        long totalProducts =
                productRepository.countByActiveTrue();

        long totalCustomers =
                customerRepository.countByActiveTrue();

        long totalSuppliers =
                supplierRepository.countByActiveTrue();

        long currentStock =
                inventoryRepository.getCurrentStock();

        long lowStockCount =
                inventoryRepository.countLowStock();

        long pendingOrders =
                orderRepository.countByStatus(OrderStatus.PENDING);

        BigDecimal todaySales =
                orderRepository.getTotalByStatusAndDate(
                        OrderStatus.CONFIRMED,
                        today
                );

        BigDecimal todayPurchases =
                purchaseRepository.getTotalByStatusAndDate(
                        PurchaseStatus.RECEIVED,
                        today
                );

        BigDecimal revenue = todaySales;

        return new DashboardResponse(
                totalProducts,
                totalCustomers,
                totalSuppliers,
                currentStock,
                lowStockCount,
                pendingOrders,
                todaySales,
                todayPurchases,
                revenue
        );
    }
}
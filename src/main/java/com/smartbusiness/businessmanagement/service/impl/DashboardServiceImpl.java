package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.response.DashboardResponse;
import com.smartbusiness.businessmanagement.entity.enums.OrderStatus;
import com.smartbusiness.businessmanagement.repository.CustomerRepository;
import com.smartbusiness.businessmanagement.repository.InventoryRepository;
import com.smartbusiness.businessmanagement.repository.OrderRepository;
import com.smartbusiness.businessmanagement.repository.ProductRepository;
import com.smartbusiness.businessmanagement.repository.SupplierRepository;
import com.smartbusiness.businessmanagement.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {

        LocalDate today = LocalDate.now();

        Long totalProducts = hasPermission("PRODUCT_READ")
                ? productRepository.countByActiveTrue()
                : null;

        Long totalCustomers = hasPermission("CUSTOMER_READ")
                ? customerRepository.countByActiveTrue()
                : null;

        Long totalSuppliers = hasPermission("SUPPLIER_READ")
                ? supplierRepository.countByActiveTrue()
                : null;

        Long currentStock = hasPermission("INVENTORY_READ")
                ? inventoryRepository.getCurrentStock()
                : null;

        Long lowStockCount = hasPermission("INVENTORY_READ")
                ? inventoryRepository.countLowStock()
                : null;

        Long pendingOrders = hasPermission("ORDER_READ")
                ? orderRepository.countByStatus(OrderStatus.PENDING)
                : null;

        BigDecimal todaySales = hasPermission("ORDER_READ")
                ? orderRepository.getTotalByStatusAndDate(
                OrderStatus.CONFIRMED,
                today
        )
                : null;

        BigDecimal revenue = hasPermission("REPORT_READ")
                ? orderRepository.getTotalByStatus(
                OrderStatus.CONFIRMED
        )
                : null;

        return new DashboardResponse(
                totalProducts,
                totalCustomers,
                totalSuppliers,
                currentStock,
                lowStockCount,
                pendingOrders,
                todaySales,
                revenue
        );
    }

    private boolean hasPermission(String permission) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals(permission)
                );
    }
}
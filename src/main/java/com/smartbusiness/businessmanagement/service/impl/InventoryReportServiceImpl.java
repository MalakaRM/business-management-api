package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.response.InventoryReportItemResponse;
import com.smartbusiness.businessmanagement.dto.response.InventoryReportResponse;
import com.smartbusiness.businessmanagement.entity.Inventory;
import com.smartbusiness.businessmanagement.repository.InventoryRepository;
import com.smartbusiness.businessmanagement.service.InventoryReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryReportServiceImpl implements InventoryReportService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public InventoryReportResponse getInventoryReport() {

        List<Inventory> inventories =
                inventoryRepository.findAllActiveWithProduct();

        List<InventoryReportItemResponse> items =
                inventories.stream()
                        .map(this::mapToResponse)
                        .toList();

        long totalProducts = inventories.size();

        long totalStockQuantity =
                inventories.stream()
                        .mapToLong(Inventory::getQuantity)
                        .sum();

        BigDecimal totalInventoryValue =
                items.stream()
                        .map(InventoryReportItemResponse::stockValue)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        return new InventoryReportResponse(
                totalProducts,
                totalStockQuantity,
                totalInventoryValue,
                items
        );
    }

    private InventoryReportItemResponse mapToResponse(
            Inventory inventory
    ) {

        BigDecimal stockValue =
                inventory.getProduct()
                        .getCostPrice()
                        .multiply(
                                BigDecimal.valueOf(
                                        inventory.getQuantity()
                                )
                        );

        boolean lowStock =
                inventory.getQuantity()
                        <= inventory.getReorderLevel();

        return new InventoryReportItemResponse(
                inventory.getProduct().getId(),
                inventory.getProduct().getName(),
                inventory.getProduct().getSku(),
                inventory.getQuantity(),
                inventory.getReorderLevel(),
                stockValue,
                lowStock
        );
    }
}
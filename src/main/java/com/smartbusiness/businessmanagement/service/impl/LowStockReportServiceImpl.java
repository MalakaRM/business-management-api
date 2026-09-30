package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.response.LowStockReportItemResponse;
import com.smartbusiness.businessmanagement.dto.response.LowStockReportResponse;
import com.smartbusiness.businessmanagement.entity.Inventory;
import com.smartbusiness.businessmanagement.repository.InventoryRepository;
import com.smartbusiness.businessmanagement.repository.LowStockReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LowStockReportServiceImpl
        implements LowStockReportService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public LowStockReportResponse getLowStockReport() {

        List<Inventory> inventories =
                inventoryRepository.findLowStock();

        List<LowStockReportItemResponse> items =
                inventories.stream()
                        .map(this::mapToResponse)
                        .toList();

        return new LowStockReportResponse(
                items.size(),
                items
        );
    }

    private LowStockReportItemResponse mapToResponse(
            Inventory inventory
    ) {

        int shortage =
                inventory.getReorderLevel()
                        - inventory.getQuantity();

        return new LowStockReportItemResponse(
                inventory.getProduct().getId(),
                inventory.getProduct().getName(),
                inventory.getProduct().getSku(),
                inventory.getQuantity(),
                inventory.getReorderLevel(),
                shortage
        );
    }
}
package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.StockAdjustmentRequest;
import com.smartbusiness.businessmanagement.dto.request.StockMovementRequest;
import com.smartbusiness.businessmanagement.dto.request.update.UpdateReorderLevelRequest;
import com.smartbusiness.businessmanagement.dto.response.InventoryResponse;
import com.smartbusiness.businessmanagement.dto.response.StockMovementResponse;
import com.smartbusiness.businessmanagement.entity.Inventory;
import com.smartbusiness.businessmanagement.entity.Product;
import com.smartbusiness.businessmanagement.entity.enums.StockMovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryService {

    StockMovementResponse recordStockMovement(StockMovementRequest request);
    InventoryResponse getInventoryByProductId(Long productId);
    List<InventoryResponse> getLowStockProducts();
    List<StockMovementResponse> getStockMovementsByProductId(Long productId);
    InventoryResponse updateReorderLevel(Long productId, UpdateReorderLevelRequest request);
    StockMovementResponse adjustStock(StockAdjustmentRequest request);
    void deductStock(Product product, int quantity, String reason);
    void increaseStock(Product product, int quantity, StockMovementType type, String reason);
    void validateStock(Product product, int quantity);
    Page<InventoryResponse> getAllInventory(int page, int size);
    Page<InventoryResponse> searchInventoryReport(
            String search,
            Pageable pageable
    );
    Page<InventoryResponse> searchLowStockReport(
            String search,
            Pageable pageable
    );

}
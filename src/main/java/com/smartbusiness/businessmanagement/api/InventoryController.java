package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.request.StockAdjustmentRequest;
import com.smartbusiness.businessmanagement.dto.request.StockMovementRequest;
import com.smartbusiness.businessmanagement.dto.request.update.UpdateReorderLevelRequest;
import com.smartbusiness.businessmanagement.dto.response.InventoryResponse;
import com.smartbusiness.businessmanagement.dto.response.StockMovementResponse;
import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/movements")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordStockMovement(
            @Valid @RequestBody StockMovementRequest request
    ) {
        StockMovementResponse response = inventoryService.recordStockMovement(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Stock movement recorded successfully",
                        response
                ));
    }

    @GetMapping("/products/{productId}")
    @PreAuthorize("hasAuthority('INVENTORY_READ')")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventory(
            @PathVariable Long productId
    ) {
        InventoryResponse response =
                inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Inventory retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAuthority('INVENTORY_READ')")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getLowStockProducts() {
        List<InventoryResponse> response = inventoryService.getLowStockProducts();
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Low-stock products retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/products/{productId}/movements")
    @PreAuthorize("hasAuthority('INVENTORY_READ')")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getStockMovements(
            @PathVariable Long productId
    ) {
        List<StockMovementResponse> response =
                inventoryService.getStockMovementsByProductId(
                        productId
                );
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Stock movements retrieved successfully",
                        response
                )
        );
    }

    @PatchMapping("/products/{productId}/reorder-level")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE')")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateReorderLevel(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateReorderLevelRequest request
    ) {
        InventoryResponse response = inventoryService
                .updateReorderLevel(productId, request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Reorder level updated successfully",
                        response
                )
        );
    }

    @PostMapping("/adjustments")
    @PreAuthorize("hasAuthority('INVENTORY_UPDATE')")
    public ResponseEntity<ApiResponse<StockMovementResponse>> adjustStock(
            @Valid @RequestBody StockAdjustmentRequest request
    ) {
        StockMovementResponse response = inventoryService.adjustStock(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Stock adjusted successfully",
                        response
                ));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('INVENTORY_READ')")
    public ResponseEntity<ApiResponse<Page<InventoryResponse>>> getAllInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<InventoryResponse> response =
                inventoryService.getAllInventory(page, size);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Inventory retrieved successfully",
                        response
                )
        );
    }
    @GetMapping("/report/inventory")
    @PreAuthorize("hasAuthority('INVENTORY_READ')")
    public ResponseEntity<ApiResponse<Page<InventoryResponse>>> getInventoryReport(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        Page<InventoryResponse> response =
                inventoryService.searchInventoryReport(
                        search.trim(),
                        PageRequest.of(page, size)
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Inventory report retrieved successfully",
                        response
                )
        );
    }
    @GetMapping("/report/low-stock")
    @PreAuthorize("hasAuthority('INVENTORY_READ')")
    public ResponseEntity<ApiResponse<Page<InventoryResponse>>> getLowStockReport(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<InventoryResponse> response =
                inventoryService.searchLowStockReport(
                        search.trim(),
                        PageRequest.of(page, size)
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Low-stock report retrieved successfully",
                        response
                )
        );
    }
}
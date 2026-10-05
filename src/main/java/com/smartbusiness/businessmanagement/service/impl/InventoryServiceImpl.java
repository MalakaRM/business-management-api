package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.request.StockAdjustmentRequest;
import com.smartbusiness.businessmanagement.dto.request.StockMovementRequest;
import com.smartbusiness.businessmanagement.dto.request.update.UpdateReorderLevelRequest;
import com.smartbusiness.businessmanagement.dto.response.InventoryResponse;
import com.smartbusiness.businessmanagement.dto.response.StockMovementResponse;
import com.smartbusiness.businessmanagement.entity.Inventory;
import com.smartbusiness.businessmanagement.entity.Product;
import com.smartbusiness.businessmanagement.entity.StockMovement;
import com.smartbusiness.businessmanagement.entity.enums.AuditAction;
import com.smartbusiness.businessmanagement.entity.enums.StockMovementType;
import com.smartbusiness.businessmanagement.exception.InsufficientStockException;
import com.smartbusiness.businessmanagement.exception.ResourceNotFoundException;
import com.smartbusiness.businessmanagement.mapper.InventoryMapper;
import com.smartbusiness.businessmanagement.repository.InventoryRepository;
import com.smartbusiness.businessmanagement.repository.ProductRepository;
import com.smartbusiness.businessmanagement.repository.StockMovementRepository;

import com.smartbusiness.businessmanagement.service.AuditService;
import com.smartbusiness.businessmanagement.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final StockMovementRepository stockMovementRepository;
    private final InventoryMapper inventoryMapper;
    private final AuditService auditService;

    @Override
    @Transactional
    public StockMovementResponse recordStockMovement(
            StockMovementRequest request
    ) {

        Product product = productRepository
                .findById(request.productId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: "
                                        + request.productId()
                        )
                );

        Inventory inventory = inventoryRepository
                .findByProductId(product.getId())
                .orElseGet(() -> createInventory(product));

        int quantityBefore = inventory.getQuantity();
        int movementQuantity = request.quantity();

        switch (request.type()) {

            case PURCHASE -> {
                inventory.setQuantity(
                        quantityBefore + movementQuantity
                );
            }

            case SALE, DAMAGE -> {

                if (quantityBefore < movementQuantity) {
                    throw new InsufficientStockException(
                            "Insufficient stock for product: "
                                    + product.getName()
                    );
                }

                inventory.setQuantity(
                        quantityBefore - movementQuantity
                );
            }

            case ADJUSTMENT -> {
                throw new IllegalArgumentException(
                        "Stock adjustment is handled separately"
                );
            }
        }

        int quantityAfter = inventory.getQuantity();

        inventoryRepository.save(inventory);

        StockMovement movement = StockMovement.builder()
                .product(product)
                .type(request.type())
                .quantity(movementQuantity)
                .quantityBefore(quantityBefore)
                .quantityAfter(quantityAfter)
                .reason(request.reason())
                .build();

        StockMovement savedMovement = stockMovementRepository.save(movement);

        return toStockMovementResponse(savedMovement);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryByProductId(
            Long productId
    ) {

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found for product id: "
                                        + productId
                        )
                );

        return inventoryMapper.toInventoryResponse(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getLowStockProducts() {

        return inventoryRepository.findLowStock()
                .stream()
                .map(inventoryMapper::toInventoryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getStockMovementsByProductId(
            Long productId
    ) {

        productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: "
                                        + productId
                        )
                );

        return stockMovementRepository
                .findByProductIdOrderByCreatedAtDesc(productId)
                .stream()
                .map(this::toStockMovementResponse)
                .toList();
    }

    @Override
    @Transactional
    public InventoryResponse updateReorderLevel(
            Long productId,
            UpdateReorderLevelRequest request
    ) {

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found for product id: "
                                        + productId
                        )
                );

        inventory.setReorderLevel(
                request.reorderLevel()
        );

        Inventory savedInventory =
                inventoryRepository.save(inventory);

        return inventoryMapper.toInventoryResponse(
                savedInventory
        );
    }

    @Override
    @Transactional
    public StockMovementResponse adjustStock(
            StockAdjustmentRequest request
    ) {

        Product product = productRepository
                .findById(request.productId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: "
                                        + request.productId()
                        )
                );

        Inventory inventory = inventoryRepository
                .findByProductId(product.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found for product id: "
                                        + product.getId()
                        )
                );

        int quantityBefore = inventory.getQuantity();
        int quantityAfter = request.newQuantity();

        int difference =
                quantityAfter - quantityBefore;

        inventory.setQuantity(quantityAfter);

        inventoryRepository.save(inventory);

        StockMovement movement = StockMovement.builder()
                .product(product)
                .type(StockMovementType.ADJUSTMENT)
                .quantity(Math.abs(difference))
                .quantityBefore(quantityBefore)
                .quantityAfter(quantityAfter)
                .reason(request.reason())
                .build();

        StockMovement savedMovement =
                stockMovementRepository.save(movement);

        auditService.log(
                AuditAction.STOCK_ADJUSTMENT,
                "Inventory",
                product.getId().toString(),
                "Adjusted stock for product: "
                        + product.getName()
                        + " from "
                        + quantityBefore
                        + " to "
                        + quantityAfter
        );

        return toStockMovementResponse(savedMovement);
    }

    @Override
    @Transactional(readOnly = true)
    public void validateStock(
            Product product,
            int quantity
    ) {

        Inventory inventory =
                inventoryRepository.findByProductId(product.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Inventory not found for product: "
                                                + product.getId()
                                )
                        );

        if (inventory.getQuantity() < quantity) {
            throw new InsufficientStockException(
                    "Insufficient stock for product: "
                            + product.getName()
            );
        }
    }

    @Override
    public Page<InventoryResponse> getAllInventory(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return inventoryRepository
                .findAllByOrderByIdAsc(pageable)
                .map(inventoryMapper::toInventoryResponse);
    }


    @Override
    @Transactional
    public void deductStock(
            Product product,
            int quantity,
            String reason
    ) {

        Inventory inventory =
                inventoryRepository.findByProductId(product.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Inventory not found for product: "
                                                + product.getId()
                                )
                        );

        if (inventory.getQuantity() < quantity) {
            throw new InsufficientStockException(
                    "Insufficient stock for product: "
                            + product.getName()
            );
        }

        int before = inventory.getQuantity();
        int after = before - quantity;

        inventory.setQuantity(after);

        inventoryRepository.save(inventory);

        StockMovement movement = StockMovement.builder()
                .product(product)
                .type(StockMovementType.SALE)
                .quantity(quantity)
                .quantityBefore(before)
                .quantityAfter(after)
                .reason(reason)
                .build();

        stockMovementRepository.save(movement);
    }

    @Override
    @Transactional
    public void increaseStock(
            Product product,
            int quantity,
            StockMovementType type,
            String reason
    ) {

        Inventory inventory =
                inventoryRepository.findByProductId(product.getId())
                        .orElseGet(() ->
                                createInventory(product)
                        );

        int before = inventory.getQuantity();
        int after = before + quantity;

        inventory.setQuantity(after);

        inventoryRepository.save(inventory);

        StockMovement movement = StockMovement.builder()
                .product(product)
                .type(type)
                .quantity(quantity)
                .quantityBefore(before)
                .quantityAfter(after)
                .reason(reason)
                .build();

        stockMovementRepository.save(movement);
    }



    private Inventory createInventory(Product product) {

        return Inventory.builder()
                .product(product)
                .quantity(0)
                .reorderLevel(10)
                .active(true)
                .build();
    }

    private StockMovementResponse toStockMovementResponse(
            StockMovement movement
    ) {

        return new StockMovementResponse(
                movement.getId(),
                movement.getProduct().getId(),
                movement.getProduct().getName(),
                movement.getType(),
                movement.getQuantity(),
                movement.getQuantityBefore(),
                movement.getQuantityAfter(),
                movement.getReason(),
                movement.getCreatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventoryResponse> searchInventoryReport(
            String search,
            Pageable pageable
    ) {

        String cleanSearch =
                search == null ? "" : search.trim();

        Page<Inventory> inventories;

        if (!cleanSearch.isEmpty()) {

            inventories =
                    inventoryRepository.searchInventoryReport(
                            cleanSearch,
                            pageable
                    );

        } else {

            inventories =
                    inventoryRepository.findAllByOrderByIdAsc(
                            pageable
                    );
        }

        return inventories.map(
                inventoryMapper::toInventoryResponse
        );
    }
    @Override
    @Transactional(readOnly = true)
    public Page<InventoryResponse> searchLowStockReport(
            String search,
            Pageable pageable
    ) {

        String cleanSearch =
                search == null ? "" : search.trim();

        Page<Inventory> inventories;

        if (!cleanSearch.isEmpty()) {

            inventories =
                    inventoryRepository.searchLowStockReport(
                            cleanSearch,
                            pageable
                    );

        } else {

            inventories =
                    inventoryRepository.findLowStockReport(
                            pageable
                    );
        }

        return inventories.map(
                inventoryMapper::toInventoryResponse
        );
    }

}
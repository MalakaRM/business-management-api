package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.request.PurchaseCreateRequest;
import com.smartbusiness.businessmanagement.dto.request.PurchaseItemRequest;
import com.smartbusiness.businessmanagement.dto.response.PurchaseResponse;
import com.smartbusiness.businessmanagement.entity.*;
import com.smartbusiness.businessmanagement.entity.enums.AuditAction;
import com.smartbusiness.businessmanagement.entity.enums.PurchaseStatus;
import com.smartbusiness.businessmanagement.entity.enums.StockMovementType;
import com.smartbusiness.businessmanagement.exception.ResourceAlreadyExistsException;
import com.smartbusiness.businessmanagement.exception.ResourceNotFoundException;
import com.smartbusiness.businessmanagement.mapper.PurchaseMapper;
import com.smartbusiness.businessmanagement.repository.*;
import com.smartbusiness.businessmanagement.service.AuditService;
import com.smartbusiness.businessmanagement.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final PurchaseMapper purchaseMapper;
    private final InventoryRepository inventoryRepository;
    private final StockMovementRepository stockMovementRepository;
    private final AuditService auditService;

    @Override
    @Transactional
    public PurchaseResponse createPurchase(PurchaseCreateRequest request) {
        // 1. Check duplicate reference number
        if (purchaseRepository.existsByReferenceNumber(
                request.referenceNumber()
        )) {
            throw new ResourceAlreadyExistsException(
                    "Purchase reference number is already registered"
            );
        }

        // 2. Find supplier
        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with id: "
                                        + request.supplierId()
                        )
                );

        // 3. Make sure supplier is active
        if (!supplier.isActive()) {
            throw new IllegalArgumentException(
                    "Cannot create purchase for inactive supplier"
            );
        }

        // 4. Create purchase entity
        Purchase purchase = purchaseMapper.toEntity(request, supplier);
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 5. Process purchase items
        for (PurchaseItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found with id: "
                                            + itemRequest.productId()
                            )
                    );

            if (!product.isActive()) {
                throw new IllegalArgumentException(
                        "Cannot purchase inactive product: "
                                + product.getName()
                );
            }
            PurchaseItem purchaseItem =
                    purchaseMapper.toItemEntity(
                            itemRequest,
                            product,
                            purchase
                    );

            purchase.getItems().add(purchaseItem);

            totalAmount = totalAmount.add(purchaseItem.getSubtotal());
        }

        // 6. Set calculated total
        purchase.setTotalAmount(totalAmount);

        // 7. Save purchase + items
        Purchase savedPurchase = purchaseRepository.save(purchase);

        return purchaseMapper.toResponse(savedPurchase);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseResponse> getAllPurchases(Pageable pageable) {
        return purchaseRepository
                .findAll(pageable)
                .map(purchaseMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse getPurchaseById(Long id) {

        Purchase purchase =
                purchaseRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Purchase not found with id: "
                                                + id
                                )
                        );

        return purchaseMapper.toResponse(purchase);
    }

    @Override
    @Transactional
    public PurchaseResponse receivePurchase(Long id) {

        Purchase purchase =
                purchaseRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Purchase not found with id: "
                                                + id
                                )
                        );

        // 1. Purchase must be DRAFT
        if (purchase.getStatus() != PurchaseStatus.DRAFT) {

            throw new IllegalArgumentException(
                    "Only DRAFT purchases can be received"
            );
        }

        // 2. Process every purchase item
        for (PurchaseItem item : purchase.getItems()) {

            Product product = item.getProduct();

            Inventory inventory =
                    inventoryRepository
                            .findByProductId(product.getId())
                            .orElseGet(() ->
                                    Inventory.builder()
                                            .product(product)
                                            .quantity(0)
                                            .reorderLevel(10)
                                            .active(true)
                                            .build()
                            );

            int quantityBefore =
                    inventory.getQuantity();

            int quantityAfter =
                    quantityBefore + item.getQuantity();

            inventory.setQuantity(quantityAfter);

            inventoryRepository.save(inventory);

            // 3. Create stock movement
            StockMovement movement =
                    StockMovement.builder()
                            .product(product)
                            .type(StockMovementType.PURCHASE)
                            .quantity(item.getQuantity())
                            .quantityBefore(quantityBefore)
                            .quantityAfter(quantityAfter)
                            .reason(
                                    "Purchase received: "
                                            + purchase.getReferenceNumber()
                            )
                            .build();

            stockMovementRepository.save(movement);
        }

        // 4. Change purchase status
        purchase.setStatus(PurchaseStatus.RECEIVED);

        Purchase savedPurchase =
                purchaseRepository.save(purchase);

        auditService.log(
                AuditAction.PURCHASE,
                "Purchase",
                savedPurchase.getId().toString(),
                "Received purchase: "
                        + savedPurchase.getReferenceNumber()
        );


        return purchaseMapper.toResponse(
                savedPurchase
        );
    }
}
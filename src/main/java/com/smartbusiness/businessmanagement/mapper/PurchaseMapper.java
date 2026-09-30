package com.smartbusiness.businessmanagement.mapper;

import com.smartbusiness.businessmanagement.dto.request.PurchaseCreateRequest;
import com.smartbusiness.businessmanagement.dto.request.PurchaseItemRequest;
import com.smartbusiness.businessmanagement.dto.response.PurchaseItemResponse;
import com.smartbusiness.businessmanagement.dto.response.PurchaseResponse;
import com.smartbusiness.businessmanagement.entity.Purchase;
import com.smartbusiness.businessmanagement.entity.PurchaseItem;
import com.smartbusiness.businessmanagement.entity.Product;
import com.smartbusiness.businessmanagement.entity.Supplier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PurchaseMapper {

    public Purchase toEntity(
            PurchaseCreateRequest request,
            Supplier supplier
    ) {

        return Purchase.builder()
                .referenceNumber(request.referenceNumber())
                .supplier(supplier)
                .purchaseDate(request.purchaseDate())
                .notes(request.notes())
                .build();
    }

    public PurchaseItem toItemEntity(
            PurchaseItemRequest request,
            Product product,
            Purchase purchase
    ) {

        BigDecimal subtotal =
                request.unitCost()
                        .multiply(
                                BigDecimal.valueOf(
                                        request.quantity()
                                )
                        );

        return PurchaseItem.builder()
                .purchase(purchase)
                .product(product)
                .quantity(request.quantity())
                .unitCost(request.unitCost())
                .subtotal(subtotal)
                .build();
    }

    public PurchaseResponse toResponse(
            Purchase purchase
    ) {

        List<PurchaseItemResponse> items =
                purchase.getItems()
                        .stream()
                        .map(this::toItemResponse)
                        .toList();

        return new PurchaseResponse(
                purchase.getId(),
                purchase.getReferenceNumber(),
                purchase.getSupplier().getId(),
                purchase.getSupplier().getName(),
                purchase.getPurchaseDate(),
                purchase.getStatus(),
                purchase.getTotalAmount(),
                purchase.getNotes(),
                items
        );
    }

    private PurchaseItemResponse toItemResponse(
            PurchaseItem item
    ) {

        return new PurchaseItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitCost(),
                item.getSubtotal()
        );
    }
}
package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.request.ProductCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.ProductResponse;
import com.smartbusiness.businessmanagement.entity.Category;
import com.smartbusiness.businessmanagement.entity.Product;
import com.smartbusiness.businessmanagement.entity.enums.AuditAction;
import com.smartbusiness.businessmanagement.exception.ResourceAlreadyExistsException;
import com.smartbusiness.businessmanagement.exception.ResourceNotFoundException;
import com.smartbusiness.businessmanagement.mapper.ProductMapper;
import com.smartbusiness.businessmanagement.repository.CategoryRepository;
import com.smartbusiness.businessmanagement.repository.InventoryRepository;
import com.smartbusiness.businessmanagement.repository.ProductRepository;
import com.smartbusiness.businessmanagement.repository.StockMovementRepository;
import com.smartbusiness.businessmanagement.service.AuditService;
import com.smartbusiness.businessmanagement.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final AuditService auditService;


    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {
        // 1. Check duplicate SKU
        if (productRepository.existsBySku(request.sku())) {
            throw new ResourceAlreadyExistsException(
                    "Product SKU is already registered"
            );
        }
        // 2. Find category
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                                "Category not found with id: "
                                        + request.categoryId()
                        )
                );

        // 3. Convert request → entity
        Product product = productMapper.toEntity(request, category);

        // 4. Save product
        Product savedProduct = productRepository.save(product);

        auditService.log(
                AuditAction.CREATE,
                "Product",
                savedProduct.getId().toString(),
                "Created product: " + savedProduct.getName()
        );

        // 5. Convert entity → response
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(
            Long productId,
            ProductCreateRequest request
    ) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found: " + productId
                        )
                );

        if (!product.getSku().equals(request.sku())
                && productRepository.existsBySku(request.sku())) {

            throw new ResourceAlreadyExistsException(
                    "Product with SKU already exists: "
                            + request.sku()
            );
        }

        Category category = categoryRepository.findById(
                request.categoryId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Category not found: "
                                + request.categoryId()
                )
        );

        product.setName(request.name());
        product.setSku(request.sku());
        product.setPrice(request.price());
        product.setCostPrice(request.costPrice());
        product.setDescription(request.description());
        product.setCategory(category);

        Product savedProduct =
                productRepository.save(product);

        auditService.log(
                AuditAction.UPDATE,
                "Product",
                savedProduct.getId().toString(),
                "Updated product: " + savedProduct.getName()
        );

        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse deactivateProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found: " + productId
                        )
                );

        if (!product.isActive()) {
            throw new IllegalArgumentException(
                    "Product is already inactive"
            );
        }

        product.setActive(false);

        Product savedProduct =
                productRepository.save(product);

        auditService.log(
                AuditAction.DEACTIVATE,
                "Product",
                savedProduct.getId().toString(),
                "Deactivated product: " + savedProduct.getName()
        );

        return productMapper.toResponse(savedProduct);
    }
}
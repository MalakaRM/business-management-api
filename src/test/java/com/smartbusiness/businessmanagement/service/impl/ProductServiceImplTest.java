package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.request.ProductCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.ProductResponse;
import com.smartbusiness.businessmanagement.entity.Category;
import com.smartbusiness.businessmanagement.entity.Product;
import com.smartbusiness.businessmanagement.entity.enums.AuditAction;
import com.smartbusiness.businessmanagement.exception.ResourceAlreadyExistsException;
import com.smartbusiness.businessmanagement.mapper.ProductMapper;
import com.smartbusiness.businessmanagement.repository.CategoryRepository;
import com.smartbusiness.businessmanagement.repository.ProductRepository;
import com.smartbusiness.businessmanagement.service.AuditService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldCreateProduct() {

        // Arrange
        ProductCreateRequest request =
                new ProductCreateRequest(
                        "Coca Cola",
                        "COKE-001",
                        new BigDecimal("250.00"),
                        new BigDecimal("180.00"),
                        "Coca Cola 500ml",
                        1L
                );

        Category category = Category.builder()
                .id(1L)
                .name("Beverages")
                .description("Drinks")
                .active(true)
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("Coca Cola")
                .sku("COKE-001")
                .price(new BigDecimal("250.00"))
                .costPrice(new BigDecimal("180.00"))
                .description("Coca Cola 500ml")
                .active(true)
                .category(category)
                .build();

        ProductResponse response =
                new ProductResponse(
                        1L,
                        "Coca Cola",
                        "COKE-001",
                        new BigDecimal("250.00"),
                        new BigDecimal("180.00"),
                        "Coca Cola 500ml",
                        true,
                        1L,
                        "Beverages"
                );

        when(productRepository.existsBySku("COKE-001"))
                .thenReturn(false);

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productMapper.toEntity(request, category))
                .thenReturn(product);

        when(productRepository.save(product))
                .thenReturn(product);

        when(productMapper.toResponse(product))
                .thenReturn(response);

        // Act
        ProductResponse result =
                productService.createProduct(request);

        // Assert
        assertEquals(response, result);

        verify(productRepository)
                .existsBySku("COKE-001");

        verify(categoryRepository)
                .findById(1L);

        verify(productRepository)
                .save(product);

        verify(auditService).log(
                eq(AuditAction.CREATE),
                eq("Product"),
                eq("1"),
                eq("Created product: Coca Cola")
        );
    }


    @Test
    void shouldThrowExceptionWhenSkuAlreadyExists() {

        // Arrange
        ProductCreateRequest request =
                new ProductCreateRequest(
                        "Coca Cola",
                        "COKE-001",
                        new BigDecimal("250.00"),
                        new BigDecimal("180.00"),
                        "Coca Cola 500ml",
                        1L
                );

        when(productRepository.existsBySku("COKE-001"))
                .thenReturn(true);

        // Act & Assert
        ResourceAlreadyExistsException exception =
                assertThrows(
                        ResourceAlreadyExistsException.class,
                        () -> productService.createProduct(request)
                );

        assertEquals(
                "Product SKU is already registered",
                exception.getMessage()
        );

        // Verify
        verify(productRepository)
                .existsBySku("COKE-001");

        verify(productRepository, never())
                .save(any(Product.class));

        verify(auditService, never())
                .log(
                        any(),
                        anyString(),
                        anyString(),
                        anyString()
                );
    }
}
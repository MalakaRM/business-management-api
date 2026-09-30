package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.request.ProductCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.ProductResponse;
import com.smartbusiness.businessmanagement.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request
    ) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Product created successfully",
                                response
                        )
                );
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductCreateRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product updated successfully",
                        productService.updateProduct(id, request)
                )
        );
    }
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    public ResponseEntity<ApiResponse<ProductResponse>> deactivateProduct(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product deactivated successfully",
                        productService.deactivateProduct(id)
                )
        );
    }
}
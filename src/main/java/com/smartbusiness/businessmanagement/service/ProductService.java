package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.ProductCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest request);
    ProductResponse updateProduct(Long productId, ProductCreateRequest request);
    ProductResponse deactivateProduct(Long productId);
    Page<ProductResponse> getAllProducts(int page, int size);
    ProductResponse getProductById(Long id);
}
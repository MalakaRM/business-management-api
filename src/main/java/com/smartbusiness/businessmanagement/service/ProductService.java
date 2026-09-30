package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.ProductCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest request);
    ProductResponse updateProduct(Long productId, ProductCreateRequest request);
    ProductResponse deactivateProduct(Long productId);
}
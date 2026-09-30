package com.smartbusiness.businessmanagement.mapper;

import com.smartbusiness.businessmanagement.dto.request.ProductCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.ProductResponse;
import com.smartbusiness.businessmanagement.entity.Category;
import com.smartbusiness.businessmanagement.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductCreateRequest request, Category category) {
        return Product.builder()
                .name(request.name())
                .sku(request.sku())
                .price(request.price())
                .costPrice(request.costPrice())
                .description(request.description())
                .category(category)
                .build();
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getPrice(),
                product.getCostPrice(),
                product.getDescription(),
                product.isActive(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}
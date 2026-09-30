package com.smartbusiness.businessmanagement.mapper;

import com.smartbusiness.businessmanagement.dto.request.CategoryCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.CategoryResponse;
import com.smartbusiness.businessmanagement.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryCreateRequest request) {

        return Category.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public CategoryResponse toResponse(Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive()
        );
    }
}
package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.CategoryCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryCreateRequest request);
    Page<CategoryResponse> getAllCategories(int page, int size);
    CategoryResponse updateCategory(Long id, CategoryCreateRequest request);
    CategoryResponse deactivateCategory(Long id);
    CategoryResponse getCategoryById(Long id);
}
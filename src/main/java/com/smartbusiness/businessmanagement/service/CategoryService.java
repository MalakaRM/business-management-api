package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.CategoryCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.CategoryResponse;

public interface CategoryService {

    CategoryResponse createCategory(CategoryCreateRequest request);
}
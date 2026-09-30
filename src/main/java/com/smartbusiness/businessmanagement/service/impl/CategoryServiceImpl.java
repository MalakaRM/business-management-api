package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.mapper.CategoryMapper;
import com.smartbusiness.businessmanagement.dto.request.CategoryCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.CategoryResponse;
import com.smartbusiness.businessmanagement.entity.Category;
import com.smartbusiness.businessmanagement.exception.ResourceAlreadyExistsException;
import com.smartbusiness.businessmanagement.repository.CategoryRepository;
import com.smartbusiness.businessmanagement.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new ResourceAlreadyExistsException(
                    "Category name is already registered"
            );
        }
        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }
}
package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.request.CategoryCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.CategoryResponse;
import com.smartbusiness.businessmanagement.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryCreateRequest request
    ) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Category created successfully",
                                response
                        )
                );
    }
}
package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.request.SupplierCreateRequest;
import com.smartbusiness.businessmanagement.dto.request.update.SupplierUpdateRequest;
import com.smartbusiness.businessmanagement.dto.response.SupplierResponse;
import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<ApiResponse<SupplierResponse>> createSupplier(
            @Valid @RequestBody SupplierCreateRequest request
    ) {
        SupplierResponse response = supplierService.createSupplier(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Supplier created successfully",
                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SupplierResponse>>> getAllSuppliers(
            @PageableDefault(
                    size = 20,
                    sort = "name",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {

        Page<SupplierResponse> response =
                supplierService.getAllSuppliers(pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Suppliers retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierResponse>> getSupplierById(
            @PathVariable Long id) {
        SupplierResponse response = supplierService.getSupplierById(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Supplier retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Page<SupplierResponse>>> getActiveSuppliers(
            @PageableDefault(
                    size = 20,
                    sort = "name",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {

        Page<SupplierResponse> response =
                supplierService.getActiveSuppliers(pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Active suppliers retrieved successfully",
                        response
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierResponse>>
    updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierUpdateRequest request
    ) {

        SupplierResponse response =
                supplierService.updateSupplier(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Supplier updated successfully",
                        response
                )
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateSupplier(@PathVariable Long id) {
        supplierService.deactivateSupplier(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Supplier deactivated successfully"
                )
        );
    }
}
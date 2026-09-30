package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.request.CustomerCreateRequest;
import com.smartbusiness.businessmanagement.dto.request.update.CustomerUpdateRequest;
import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.CustomerResponse;
import com.smartbusiness.businessmanagement.service.CustomerService;
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
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>>
    createCustomer(
            @Valid @RequestBody CustomerCreateRequest request
    ) {

        CustomerResponse response =
                customerService.createCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Customer created successfully",
                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<
            ApiResponse<Page<CustomerResponse>>
            >
    getAllCustomers(

            @PageableDefault(
                    size = 20,
                    sort = "name",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {

        Page<CustomerResponse> response =
                customerService.getAllCustomers(
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customers retrieved successfully",
                        response
                )
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>>
    getCustomerById(
            @PathVariable Long id
    ) {

        CustomerResponse response =
                customerService.getCustomerById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/active")
    public ResponseEntity<
            ApiResponse<Page<CustomerResponse>>
            >
    getActiveCustomers(

            @PageableDefault(
                    size = 20,
                    sort = "name",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {

        Page<CustomerResponse> response =
                customerService.getActiveCustomers(
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Active customers retrieved successfully",
                        response
                )
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>>
    updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {

        CustomerResponse response =
                customerService.updateCustomer(
                        id,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer updated successfully",
                        response
                )
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>>
    deactivateCustomer(
            @PathVariable Long id
    ) {

        customerService.deactivateCustomer(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer deactivated successfully"
                )
        );
    }
}


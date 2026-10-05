package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.request.PurchaseCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.PurchaseResponse;
import com.smartbusiness.businessmanagement.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PreAuthorize("hasAuthority('PURCHASE_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseResponse>>
    createPurchase(
            @Valid @RequestBody PurchaseCreateRequest request
    ) {

        PurchaseResponse response =
                purchaseService.createPurchase(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Purchase created successfully",
                                response
                        )
                );
    }

    @PreAuthorize("hasAuthority('PURCHASE_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PurchaseResponse>>>
    getAllPurchases(
            @PageableDefault(
                    size = 20,
                    sort = "purchaseDate",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {

        Page<PurchaseResponse> response =
                purchaseService.getAllPurchases(pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Purchases retrieved successfully",
                        response
                )
        );
    }

    @PreAuthorize("hasAuthority('PURCHASE_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseResponse>>
    getPurchaseById(
            @PathVariable Long id
    ) {

        PurchaseResponse response =
                purchaseService.getPurchaseById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Purchase retrieved successfully",
                        response
                )
        );
    }
    @PreAuthorize("hasAuthority('PURCHASE_UPDATE')")
    @PatchMapping("/{id}/receive")
    public ResponseEntity<ApiResponse<PurchaseResponse>>
    receivePurchase(
            @PathVariable Long id
    ) {

        PurchaseResponse response =
                purchaseService.receivePurchase(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Purchase received successfully",
                        response
                )
        );
    }
    @PreAuthorize("hasAuthority('PURCHASE_DELETE')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<PurchaseResponse>>
    cancelPurchase(
            @PathVariable Long id
    ) {

        PurchaseResponse response =
                purchaseService.cancelPurchase(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Purchase cancelled successfully",
                        response
                )
        );
    }
    @PreAuthorize("hasAuthority('PURCHASE_CREATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseResponse>>
    updatePurchase(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseCreateRequest request
    ) {

        PurchaseResponse response =
                purchaseService.updatePurchase(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Purchase updated successfully",
                        response
                )
        );
    }
    @PreAuthorize("hasAuthority('PURCHASE_READ')")
    @GetMapping("/report/purchases")
    public ResponseEntity<ApiResponse<Page<PurchaseResponse>>> getPurchaseReport(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(defaultValue = "") String search,
            @PageableDefault(
                    size = 20,
                    sort = "purchaseDate",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {

        Page<PurchaseResponse> response =
                purchaseService.searchPurchaseReport(
                        from,
                        to,
                        search.trim(),
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Purchase report retrieved successfully",
                        response
                )
        );
    }
}
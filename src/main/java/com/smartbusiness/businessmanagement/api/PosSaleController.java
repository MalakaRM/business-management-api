package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.request.PosSaleRequest;
import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.PosSaleResponse;
import com.smartbusiness.businessmanagement.service.PosSaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pos")
@RequiredArgsConstructor
public class PosSaleController {

    private final PosSaleService posSaleService;

    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    @PostMapping("/complete-sale")
    public ResponseEntity<ApiResponse<PosSaleResponse>> completeSale(
            @Valid @RequestBody PosSaleRequest request
    ) {

        PosSaleResponse response =
                posSaleService.completeSale(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Sale completed successfully",
                        response
                )
        );
    }
}
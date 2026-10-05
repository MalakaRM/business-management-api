package com.smartbusiness.businessmanagement.api;

import com.smartbusiness.businessmanagement.dto.request.OrderCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.ApiResponse;
import com.smartbusiness.businessmanagement.dto.response.OrderResponse;
import com.smartbusiness.businessmanagement.service.OrderService;
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
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>>
    createOrder(
            @Valid @RequestBody OrderCreateRequest request
    ) {

        OrderResponse response =
                orderService.createOrder(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Order created successfully",
                                response
                        )
                );
    }
    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmOrder(@PathVariable Long id) {

        OrderResponse response =
                orderService.confirmOrder(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order confirmed successfully",
                        response
                )
        );

    }

    @PreAuthorize("hasAuthority('ORDER_READ')")
    @GetMapping
    public ResponseEntity<
            ApiResponse<Page<OrderResponse>>
            > getAllOrders(

            @PageableDefault(
                    size = 20,
                    sort = "orderDate",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {

        Page<OrderResponse> response =
                orderService.getAllOrders(pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Orders retrieved successfully",
                        response
                )
        );
    }

    @PreAuthorize("hasAuthority('ORDER_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<
            ApiResponse<OrderResponse>
            > getOrderById(
            @PathVariable Long id
    ) {

        OrderResponse response =
                orderService.getOrderById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order retrieved successfully",
                        response
                )
        );
    }

    @PreAuthorize("hasAuthority('ORDER_DELETE')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>>
    cancelOrder(
            @PathVariable Long id
    ) {

        orderService.cancelOrder(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order cancelled successfully"
                )
        );
    }

    @PreAuthorize("hasAuthority('ORDER_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>>
    updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderCreateRequest request
    ) {

        OrderResponse response =
                orderService.updateOrder(
                        id,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order updated successfully",
                        response
                )
        );
    }
    @PreAuthorize("hasAuthority('ORDER_READ')")
    @GetMapping("/report/sales")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getSalesReport(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(defaultValue = "") String search,
            @PageableDefault(
                    size = 20,
                    sort = "orderDate",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {

        Page<OrderResponse> response =
                orderService.searchSalesReport(
                        from,
                        to,
                        search.trim(),
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Sales report retrieved successfully",
                        response
                )
        );
    }
}
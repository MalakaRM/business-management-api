package com.smartbusiness.businessmanagement.mapper;

import com.smartbusiness.businessmanagement.dto.request.SupplierCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.SupplierResponse;
import com.smartbusiness.businessmanagement.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public Supplier toEntity(SupplierCreateRequest request) {

        return Supplier.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .build();
    }

    public SupplierResponse toResponse(Supplier supplier) {

        return new SupplierResponse(
                supplier.getId(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getAddress(),
                supplier.isActive()
        );
    }
}
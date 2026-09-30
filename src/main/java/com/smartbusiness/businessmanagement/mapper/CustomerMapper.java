package com.smartbusiness.businessmanagement.mapper;

import com.smartbusiness.businessmanagement.dto.request.CustomerCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.CustomerResponse;
import com.smartbusiness.businessmanagement.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(
            CustomerCreateRequest request
    ) {
        return Customer.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .build();
    }

    public CustomerResponse toResponse(
            Customer customer
    ) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.isActive()
        );
    }
}
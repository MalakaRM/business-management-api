package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.CustomerCreateRequest;
import com.smartbusiness.businessmanagement.dto.request.update.CustomerUpdateRequest;
import com.smartbusiness.businessmanagement.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomerService {

    CustomerResponse createCustomer(CustomerCreateRequest request);

    Page<CustomerResponse> getAllCustomers(
            Pageable pageable
    );

    CustomerResponse getCustomerById(
            Long id
    );

    Page<CustomerResponse> getActiveCustomers(
            Pageable pageable
    );

    CustomerResponse updateCustomer(
            Long id,
            CustomerUpdateRequest request
    );

    void deactivateCustomer(
            Long id
    );
}
package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.request.CustomerCreateRequest;
import com.smartbusiness.businessmanagement.dto.request.update.CustomerUpdateRequest;
import com.smartbusiness.businessmanagement.dto.response.CustomerResponse;
import com.smartbusiness.businessmanagement.entity.Customer;
import com.smartbusiness.businessmanagement.exception.ResourceAlreadyExistsException;
import com.smartbusiness.businessmanagement.exception.ResourceNotFoundException;
import com.smartbusiness.businessmanagement.mapper.CustomerMapper;
import com.smartbusiness.businessmanagement.repository.CustomerRepository;
import com.smartbusiness.businessmanagement.service.CustomerService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponse createCustomer(
            CustomerCreateRequest request
    ) {

        if (request.email() != null
                && customerRepository.existsByEmail(
                request.email()
        )) {

            throw new ResourceAlreadyExistsException(
                    "Customer email is already registered"
            );
        }

        Customer customer =
                customerMapper.toEntity(request);

        Customer savedCustomer =
                customerRepository.save(customer);

        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getAllCustomers(
            Pageable pageable
    ) {

        return customerRepository
                .findAll(pageable)
                .map(customerMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(
            Long id
    ) {

        Customer customer =
                customerRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + id
                                )
                        );

        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getActiveCustomers(Pageable pageable) {
        return customerRepository
                .findByActiveTrue(pageable)
                .map(customerMapper::toResponse);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(
            Long id,
            CustomerUpdateRequest request
    ) {

        Customer customer =
                customerRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + id
                                )
                        );

        if (!Objects.equals(
                customer.getEmail(),
                request.email()
        )
                && request.email() != null
                && customerRepository.existsByEmail(
                request.email()
        )) {

            throw new ResourceAlreadyExistsException(
                    "Customer email is already registered"
            );
        }

        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setAddress(request.address());

        Customer updatedCustomer =
                customerRepository.save(customer);

        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    @Transactional
    public void deactivateCustomer(
            Long id
    ) {

        Customer customer =
                customerRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + id
                                )
                        );

        customer.setActive(false);

        customerRepository.save(customer);
    }
}


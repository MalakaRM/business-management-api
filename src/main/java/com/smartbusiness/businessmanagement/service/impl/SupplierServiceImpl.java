package com.smartbusiness.businessmanagement.service.impl;

import com.smartbusiness.businessmanagement.dto.request.SupplierCreateRequest;
import com.smartbusiness.businessmanagement.dto.request.update.SupplierUpdateRequest;
import com.smartbusiness.businessmanagement.dto.response.SupplierResponse;
import com.smartbusiness.businessmanagement.entity.Supplier;
import com.smartbusiness.businessmanagement.exception.ResourceAlreadyExistsException;
import com.smartbusiness.businessmanagement.exception.ResourceNotFoundException;
import com.smartbusiness.businessmanagement.mapper.SupplierMapper;
import com.smartbusiness.businessmanagement.repository.SupplierRepository;
import com.smartbusiness.businessmanagement.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    @Transactional
    public SupplierResponse createSupplier(SupplierCreateRequest request) {
        if (supplierRepository.existsByName(request.name())) {
            throw new ResourceAlreadyExistsException(
                    "Supplier name is already registered"
            );
        }
        Supplier supplier = supplierMapper.toEntity(request);

        Supplier savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toResponse(savedSupplier);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierResponse> getAllSuppliers(Pageable pageable) {
        return supplierRepository
                .findAllByOrderByIdAsc(pageable)
                .map(supplierMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = supplierRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with id: " + id
                        )
                );

        return supplierMapper.toResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierResponse> getActiveSuppliers(Pageable pageable) {
        return supplierRepository
                .findByActiveTrue(pageable)
                .map(supplierMapper::toResponse);
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(
            Long id,
            SupplierUpdateRequest request
    ) {

        Supplier supplier = supplierRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with id: " + id
                        )
                );

        if (!supplier.getName().equals(request.name())
                && supplierRepository.existsByName(request.name())) {

            throw new ResourceAlreadyExistsException(
                    "Supplier name is already registered"
            );
        }

        supplier.setName(request.name());
        supplier.setEmail(request.email());
        supplier.setPhone(request.phone());
        supplier.setAddress(request.address());

        Supplier updatedSupplier =
                supplierRepository.save(supplier);

        return supplierMapper.toResponse(updatedSupplier);
    }
    @Override
    @Transactional
    public void deactivateSupplier(Long id) {

        Supplier supplier = supplierRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with id: " + id
                        )
                );

        supplier.setActive(false);

        supplierRepository.save(supplier);
    }
}
package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.SupplierCreateRequest;
import com.smartbusiness.businessmanagement.dto.request.update.SupplierUpdateRequest;
import com.smartbusiness.businessmanagement.dto.response.SupplierResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierService {

    SupplierResponse createSupplier(SupplierCreateRequest request);
    Page<SupplierResponse> getAllSuppliers(Pageable pageable);
    SupplierResponse getSupplierById(Long id);
    Page<SupplierResponse> getActiveSuppliers(Pageable pageable);
    SupplierResponse updateSupplier(Long id, SupplierUpdateRequest request);
    void deactivateSupplier(Long id);

}
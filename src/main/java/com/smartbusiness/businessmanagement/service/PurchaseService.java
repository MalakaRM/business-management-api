package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.PurchaseCreateRequest;
import com.smartbusiness.businessmanagement.dto.response.PurchaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PurchaseService {

    PurchaseResponse createPurchase(
            PurchaseCreateRequest request
    );
    Page<PurchaseResponse> getAllPurchases(
            Pageable pageable
    );

    PurchaseResponse getPurchaseById(
            Long id
    );
    PurchaseResponse receivePurchase(
            Long id
    );
    PurchaseResponse cancelPurchase(Long id);
    PurchaseResponse updatePurchase(Long id, PurchaseCreateRequest request);
    Page<PurchaseResponse> searchPurchaseReport(
            LocalDate from,
            LocalDate to,
            String search,
            Pageable pageable
    );
}
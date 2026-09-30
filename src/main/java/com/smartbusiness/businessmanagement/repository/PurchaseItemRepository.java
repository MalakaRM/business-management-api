package com.smartbusiness.businessmanagement.repository;

import com.smartbusiness.businessmanagement.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseItemRepository
        extends JpaRepository<PurchaseItem, Long> {
}
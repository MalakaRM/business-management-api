package com.smartbusiness.businessmanagement.repository;

import com.smartbusiness.businessmanagement.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {
}
package com.smartbusiness.businessmanagement.mapper;

import com.smartbusiness.businessmanagement.dto.response.InventoryResponse;
import com.smartbusiness.businessmanagement.entity.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {
    public InventoryResponse toInventoryResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(),
                inventory.getProduct().getId(),
                inventory.getProduct().getName(),
                inventory.getQuantity(),
                inventory.getReorderLevel(),
                inventory.getQuantity()
                        <= inventory.getReorderLevel(),
                inventory.isActive()
        );
    }
}

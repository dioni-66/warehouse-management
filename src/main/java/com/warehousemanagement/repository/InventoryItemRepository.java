package com.warehousemanagement.repository;

import com.warehousemanagement.entity.InventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, Integer> {
    InventoryItemEntity findByName(String name);
}

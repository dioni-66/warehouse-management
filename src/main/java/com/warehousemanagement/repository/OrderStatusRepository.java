package com.warehousemanagement.repository;

import com.warehousemanagement.entity.OrderStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatusEntity, Integer> {

    OrderStatusEntity findByStatus(String status);
}

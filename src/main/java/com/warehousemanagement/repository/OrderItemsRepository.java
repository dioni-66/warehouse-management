package com.warehousemanagement.repository;

import com.warehousemanagement.entity.OrderItemsEntity;
import com.warehousemanagement.entity.OrderItemsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItemsEntity, OrderItemsId> {

    OrderItemsEntity getByOrder_OrderNumberAndInventoryItem_Id(Integer orderNumber, Integer inventoryItemId);

    List<OrderItemsEntity> getByOrder_OrderNumberIn(List<Integer> orderNumbers);

}

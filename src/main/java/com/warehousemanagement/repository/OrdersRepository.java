package com.warehousemanagement.repository;

import com.warehousemanagement.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersEntity, Integer> {

    List<OrdersEntity> getAllByUser_Id(int userId);

    List<OrdersEntity> getAllByUser_IdAndOrderStatus_Status(int userId, String orderStatus);

    List<OrdersEntity> getAllByOrderStatus_StatusOrderBySubmittedDateDesc(String orderStatus);

    List<OrdersEntity> findAllByOrderBySubmittedDateDesc();


}

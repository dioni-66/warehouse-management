package com.warehousemanagement.repository;

import com.warehousemanagement.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Integer> {

    List<DeliveryEntity> getAllByDeliveryDateIs(LocalDate deliveryDate);
}

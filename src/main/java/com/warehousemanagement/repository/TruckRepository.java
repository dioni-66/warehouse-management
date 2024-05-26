package com.warehousemanagement.repository;

import com.warehousemanagement.entity.TruckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TruckRepository extends JpaRepository<TruckEntity, Integer> {

    TruckEntity getByLicensePlateEquals(String licensePlate);
}

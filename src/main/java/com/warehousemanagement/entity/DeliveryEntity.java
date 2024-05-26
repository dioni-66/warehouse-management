package com.warehousemanagement.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "delivery")
public class DeliveryEntity {
    private int id;
    private LocalDate deliveryDate;
    private List<DeliveryTruckEntity> deliveryTrucks;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "delivery_date")
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryEntity that = (DeliveryEntity) o;
        return id == that.id && Objects.equals(deliveryDate, that.deliveryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deliveryDate);
    }

    @OneToMany(mappedBy = "delivery")
    public List<DeliveryTruckEntity> getDeliveryTrucks() {
        return deliveryTrucks;
    }

    public void setDeliveryTrucks(List<DeliveryTruckEntity> deliveryTrucksById) {
        this.deliveryTrucks = deliveryTrucksById;
    }
}

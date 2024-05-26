package com.warehousemanagement.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "delivery_truck")
public class DeliveryTruckEntity {
    private int id;
    private DeliveryEntity delivery;
    private TruckEntity truck;
    private List<DeliveryTruckOrderEntity> deliveryTruckOrders;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryTruckEntity that = (DeliveryTruckEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @ManyToOne
    @JoinColumn(name = "delivery_id", referencedColumnName = "id")
    public DeliveryEntity getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryEntity deliveryByDeliveryId) {
        this.delivery = deliveryByDeliveryId;
    }

    @ManyToOne
    @JoinColumn(name = "truck_id", referencedColumnName = "id")
    public TruckEntity getTruck() {
        return truck;
    }

    public void setTruck(TruckEntity truckByTruckId) {
        this.truck = truckByTruckId;
    }

    @OneToMany(mappedBy = "deliveryTruck")
    public List<DeliveryTruckOrderEntity> getDeliveryTruckOrders() {
        return deliveryTruckOrders;
    }

    public void setDeliveryTruckOrders(List<DeliveryTruckOrderEntity> deliveryTruckOrdersById) {
        this.deliveryTruckOrders = deliveryTruckOrdersById;
    }
}

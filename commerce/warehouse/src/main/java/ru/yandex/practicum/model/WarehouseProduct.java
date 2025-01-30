package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "warehouse_products")
public class WarehouseProduct {
    @Id
    @Column(name = "id")
    private UUID productId;
    @Column(name = "fragile")
    private boolean fragile;
    @Column(name = "width")
    private Double width;
    @Column(name = "height")
    private Double height;
    @Column(name = "depth")
    private Double depth;
    @Column(name = "weight")
    private Double weight;
    @Column(name = "quantity")
    private int quantity = 0;
}
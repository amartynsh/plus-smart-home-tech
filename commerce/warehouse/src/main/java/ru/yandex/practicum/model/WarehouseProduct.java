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
    private boolean fragile;
    private Double width;
    private Double height;
    private Double depth;
    private Double weight;
    private int quantity = 0;
}
package ru.yandex.practicum.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;
    private String productName;
    private String description;
    private String imageSrc;
    @Enumerated(value = EnumType.STRING)
    private QuantityState quantityState;
    @Column(name = "product_state")
    private ProductState productState;
    private double rating;
    @Column(name = "category")
    private ProductCategory productCategory;
    private double price;
}

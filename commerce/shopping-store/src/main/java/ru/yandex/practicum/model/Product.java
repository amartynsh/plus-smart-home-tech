package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID productId;
    @Column(name = "name")
    private String productName;
    @Column(name = "description")
    private String description;
    @Column(name = "image_src")
    private String imageSrc;
    @Column(name = "quantity_state")
    @Enumerated(value = EnumType.STRING)
    private QuantityState quantityState;
    @Column(name = "product_state")
    private ProductState productState;
    @Column(name = "rating")
    private double rating;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "category")
    private ProductCategory productCategory;
    @Column(name = "price")
    private double price;
}

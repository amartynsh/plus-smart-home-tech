package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    UUID orderId;
    @ElementCollection
    @CollectionTable(name = "products_orders", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @Builder.Default
    Map<UUID, Long> products = new HashMap<>();
    @Column(name = "cart_id")
    UUID shoppingCartId;
    @Column(name = "payment_id")
    UUID paymentId;
    @Column(name = "delivery_id")
    UUID deliveryId;
    @Column(name = "order_state")
    OrderState state;
    @Column(name = "delivery_weight")
    Double deliveryWeight;
    @Column(name = "delivery_volume")
    Double deliveryVolume;
    @Column(name = "fragile")
    Boolean fragile;
    @Column(name = "total_price")
    Double totalPrice;
    @Column(name = "delivery_price")
    Double deliveryPrice;
    @Column(name = "product_price")
    Double productPrice;
}
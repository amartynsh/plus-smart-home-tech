package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID orderId;
    @ElementCollection
    @CollectionTable(name = "products_orders", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @Builder.Default
    private  Map<UUID, Integer> products = new HashMap<>();
    @Column(name = "cart_id")
    private UUID shoppingCartId;
    @Column(name = "username")
    private String username;
    @Column(name = "payment_id")
    private   UUID paymentId;
    @Column(name = "delivery_id")
    private  UUID deliveryId;
    @Column(name = "order_state")
    private OrderState state;
    @Column(name = "delivery_weight")
    private Double deliveryWeight;
    @Column(name = "delivery_volume")
    private Double deliveryVolume;
    @Column(name = "fragile")
    private  Boolean fragile;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "delivery_price")
    private Double deliveryPrice;
    @Column(name = "product_price")
    private Double productPrice;
    @OneToOne
    Address address;

}
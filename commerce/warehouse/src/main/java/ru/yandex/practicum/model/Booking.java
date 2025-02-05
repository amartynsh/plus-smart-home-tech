package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking {
    @Column(name = "id")
    private UUID id;
    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "delivery_id")
    private UUID deliveryId;
    @ElementCollection
    @CollectionTable(name = "products_bookings", joinColumns = @JoinColumn(name = "booking_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    @Builder.Default
    private Map<UUID, Integer> products = new HashMap<>();
}

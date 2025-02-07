package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.model.delivery.DeliveryState;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    UUID deliveryId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "from_address_id")
    Address fromAddress;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "to_address_id")
    Address toAddress;
    @Column(name = "order_id")
    UUID orderId;
    @Enumerated(EnumType.STRING)
    DeliveryState deliveryState;

    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryId=" + deliveryId +
                ", fromAddress=" + fromAddress +
                ", toAddress=" + toAddress +
                ", orderId=" + orderId +
                ", deliveryState=" + deliveryState +
                '}';
    }
}

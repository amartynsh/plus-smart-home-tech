package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "payments")
public class Payment {
    @Id
    @Column(name = "id")
    private UUID paymentId;
    @Column(name = "total_payment")
    private Double totalPayment;
    @Column(name = "delivery_price")
    private Double deliveryPrice;
    @Column(name = "fee_total")
    private Double feeTotal;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_state")
    private PaymentState paymentState;
    @Column(name= "order_id")
    private UUID orderId;
}

package ru.yandex.practicum.model.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class PaymentDto {
    private UUID paymentId;
    private Double totalPayment;
    private Double deliveryPrice;
    private Double feeTotal;
}
package ru.yandex.practicum.model;

import java.util.Map;
import java.util.UUID;

public class OrderDto {
    UUID orderId;
    Map<UUID, Long> products;
    UUID shoppingCartId;
    UUID paymentId;
    UUID deliveryId;
    OrderState state;
    Double deliveryWeight;
    Double deliveryVolume;
    Boolean fragile;
    Double totalPrice;
    Double deliveryPrice;
    Double productPrice;
}

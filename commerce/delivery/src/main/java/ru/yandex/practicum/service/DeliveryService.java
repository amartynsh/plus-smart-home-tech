package ru.yandex.practicum.service;

import ru.yandex.practicum.model.OrderDto;
import ru.yandex.practicum.model.delivery.DeliveryDto;

import java.util.UUID;

public interface DeliveryService {
    DeliveryDto createNewDelivery(DeliveryDto deliveryDto);

    void successfulDelivery(UUID orderId);

    void failedDelivery(UUID orderId);

    void deliverPiked(UUID orderId);

    Double calcDeliveryPrice(OrderDto order);
}

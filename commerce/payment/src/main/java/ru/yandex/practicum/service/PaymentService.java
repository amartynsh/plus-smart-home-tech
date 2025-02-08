package ru.yandex.practicum.service;

import ru.yandex.practicum.model.OrderDto;
import ru.yandex.practicum.model.payment.PaymentDto;

import java.util.UUID;

public interface PaymentService {
    PaymentDto addNewPayment(OrderDto orderDto);

    void paymentFailed(UUID orderId);

    void paymentSuccess(UUID orderId);

    double getTotalCostPrice(OrderDto orderDto);
}

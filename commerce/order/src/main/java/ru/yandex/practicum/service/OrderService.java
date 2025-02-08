package ru.yandex.practicum.service;

import ru.yandex.practicum.model.CreateNewOrderRequest;
import ru.yandex.practicum.model.OrderDto;
import ru.yandex.practicum.model.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderDto> findBy(String username);

    OrderDto addNewOrder(CreateNewOrderRequest request);

    OrderDto returnProductToWarehouse(ProductReturnRequest request);

    OrderDto setPaymentSuccess(UUID paymentId);

    OrderDto setPaymentFailed(UUID paymentId);

    OrderDto setOrderStateDelivered(UUID orderId);

    OrderDto setOrderDeliveryFailed(UUID orderId);

    OrderDto setOrderCompleted(UUID orderId);

    OrderDto getOrderTotalPrice(UUID orderId);

    OrderDto getOrderDeliveryPrice(UUID orderId);

    OrderDto orderAssembly(UUID orderId);

    OrderDto assemblyFailed(UUID orderId);
}

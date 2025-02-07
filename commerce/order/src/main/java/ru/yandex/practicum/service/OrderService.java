package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import ru.yandex.practicum.model.CreateNewOrderRequest;
import ru.yandex.practicum.model.OrderDto;
import ru.yandex.practicum.model.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderDto> findBy(String username);

    @Transactional
    OrderDto addNewOrder(CreateNewOrderRequest request);

    @Transactional
    OrderDto returnProductToWarehouse(ProductReturnRequest request);


    @Transactional
    OrderDto setPaymentSuccess(UUID paymentId);

    @Transactional
    OrderDto setPaymentFailed(UUID paymentId);

    @Transactional
    OrderDto setOrderStateDelivered(UUID orderId);

    @Transactional
    OrderDto setOrderDeliveryFailed(UUID orderId);

    @Transactional
    OrderDto setOrderCompleted(UUID orderId);

    @Transactional
    OrderDto getOrderTotalPrice(UUID orderId);

    @Transactional
    OrderDto getOrderDeliveryPrice(UUID orderId);

    @Transactional
    OrderDto orderAssembly(UUID orderId);
}

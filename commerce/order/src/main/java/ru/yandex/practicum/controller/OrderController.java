package ru.yandex.practicum.controller;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.clients.OrderClient;
import ru.yandex.practicum.model.CreateNewOrderRequest;
import ru.yandex.practicum.model.OrderDto;
import ru.yandex.practicum.model.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;
import ru.yandex.practicum.service.OrderServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/v1/order")
public class OrderController implements OrderClient {
    private final OrderService orderService;

    @Override
    public List<OrderDto> getOrderBy(String username) {
        log.info("Обращение на GET /api/v1/order, username = {}", username);
        return orderService.findBy(username);
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        log.info("Обращение на PUT /api/v1/order, request = {}", request);
        return orderService.addNewOrder(request);
    }

    @Override
    public OrderDto returnProduct(ProductReturnRequest request) {

        return orderService.returnProductToWarehouse(request);
    }

    @Override
    public OrderDto payment(UUID paymentId) {
        return orderService.setPaymentSuccess(paymentId);
    }

    @Override
    public OrderDto failedPayment(UUID paymentId) {
        return  orderService.setPaymentFailed(paymentId);
    }

    @Override
    public OrderDto orderDelivered(UUID orderId) {
        return  orderService.setOrderStateDelivered(orderId);
    }

    @Override
    public OrderDto orderDeliveryFailed(UUID orderId) {
        return orderService.setOrderDeliveryFailed(orderId);
    }

    @Override
    public OrderDto orderCompleted(UUID orderId) {
        return orderService.setOrderCompleted(orderId);
    }

    @Override
    public OrderDto calculateTotal(UUID orderId) {
        return orderService.getOrderTotalPrice(orderId);
    }

    @Override
    public OrderDto calculateDelivery(UUID orderId) {
        return orderService.getOrderDeliveryPrice(orderId);
    }

    @Override
    public OrderDto orderAssembly(UUID orderId) {
        return orderService.orderAssembly(orderId);
    }

    @Override
    public OrderDto orderAssemblyFailed(@NotNull UUID orderId) {
        return orderService.assemblyFailed(orderId);
    }
}

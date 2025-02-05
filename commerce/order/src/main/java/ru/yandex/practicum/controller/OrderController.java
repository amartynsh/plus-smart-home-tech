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
import ru.yandex.practicum.service.OrderServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/v1/order")
public class OrderController implements OrderClient {
    private final OrderServiceImpl orderService;
    @Override
    public List<OrderDto> getOrderBy(String username) {
        log.info("Обращение на GET /api/v1/order, username = {}", username);
        return orderService.findBy(username);
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        return null;
    }

    @Override
    public OrderDto returnProduct(ProductReturnRequest request) {
        return null;
    }

    @Override
    public OrderDto payment(UUID paymentId) {
        return null;
    }

    @Override
    public OrderDto failedPayment(UUID paymentId) {
        return null;
    }

    @Override
    public OrderDto orderDelivered(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto orderCompleted(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto calculateTotal(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto calculateDelivery(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto orderAssembly(UUID orderId) {
        return null;
    }

    @Override
    public OrderDto orderAssemblyFailed(@NotNull UUID orderId) {
        return null;
    }
}

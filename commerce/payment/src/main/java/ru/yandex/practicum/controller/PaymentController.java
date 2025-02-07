package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.clients.PaymentClient;
import ru.yandex.practicum.model.OrderDto;
import ru.yandex.practicum.model.payment.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@Slf4j
@AllArgsConstructor
public class PaymentController implements PaymentClient {
    private final PaymentService paymentService;

    @Override
    public PaymentDto createNewPayment(OrderDto orderDto) {
        return paymentService.addNewPayment(orderDto);
    }

    @Override
    public Double calculateTotalCost(OrderDto orderDto) {
        return paymentService.getTotalCostPrice(orderDto);
    }

    @Override
    public void makeRefund(UUID orderId) {
        paymentService.paymentSuccess(orderId);

    }

    @Override
    public Double calculateProductCost(OrderDto orderDto) {
        return paymentService.getTotalCostPrice(orderDto);
    }

    @Override
    public void failedOrder(UUID orderId) {
        paymentService.paymentFailed(orderId);
    }
}
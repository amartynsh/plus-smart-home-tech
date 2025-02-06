package ru.yandex.practicum.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.model.OrderDto;
import ru.yandex.practicum.model.payment.PaymentDto;

import java.util.UUID;

@FeignClient(name = "payment")
public interface PaymentClient {
    @PostMapping
    PaymentDto createNewPayment(@RequestBody OrderDto orderDto);

    @PostMapping("/totalCost")
    Double calculateTotalCost(@RequestBody OrderDto orderDto);

    @PostMapping("/refund")
    void makeRefund(@RequestBody UUID orderId);

    @PostMapping("/productCost")
    Double calculateProductCost(@RequestBody OrderDto orderDto);

    @PostMapping("/failed")
    void failedOrder(@RequestBody UUID orderId);
}

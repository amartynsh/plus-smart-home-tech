package ru.yandex.practicum.clients;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.CreateNewOrderRequest;
import ru.yandex.practicum.model.OrderDto;
import ru.yandex.practicum.model.ProductReturnRequest;


import java.util.List;
import java.util.UUID;

@FeignClient(name = "order")
public interface OrderClient {

    @GetMapping
    List<OrderDto> getOrderBy(@RequestParam("username") String username);

    @PutMapping
    OrderDto createOrder (@RequestBody CreateNewOrderRequest request);

    @PostMapping("/return")
    OrderDto returnProduct(@RequestBody ProductReturnRequest request);

    @PostMapping("/payment")
    OrderDto payment(@RequestBody @NotNull UUID paymentId);

    @PostMapping("/payment/failed")
    OrderDto failedPayment(@RequestBody @NotNull UUID paymentId);

    @PostMapping("/delivery")
    OrderDto orderDelivered(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto orderDeliveryFailed(@RequestBody @NotNull UUID orderId);

    @PostMapping("/completed")
    OrderDto orderCompleted (@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateTotal(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto calculateDelivery(@RequestBody @NotNull UUID orderId);

    @PostMapping("/order/assembly")
    OrderDto orderAssembly(@RequestBody @NotNull UUID orderId);

    @PostMapping("/order/assembly/failed")
    OrderDto orderAssemblyFailed(@RequestBody @NotNull UUID orderId);









}

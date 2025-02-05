package ru.yandex.practicum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.model.delivery.DeliveryDto;

import java.util.UUID;

@FeignClient(name = "delivery")
public interface DeliveryClient {

    @PutMapping
    DeliveryDto createNewDelivery(DeliveryDto deliveryDto);

    @PostMapping("/successful")
    void successfulDelivery(UUID orderId);

    @PostMapping("/picked")
    void pickedDelivery(UUID orderId);

    @PostMapping("/failed")
    void failedDelivery(UUID orderId);

    @PostMapping("/cost")
    Double costDelivery(UUID orderId);






}

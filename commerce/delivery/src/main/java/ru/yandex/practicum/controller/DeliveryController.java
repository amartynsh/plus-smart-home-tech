package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.clients.DeliveryClient;
import ru.yandex.practicum.model.delivery.DeliveryDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/delivery")
@AllArgsConstructor
public class DeliveryController implements DeliveryClient {
    private final DeliveryService deliveryService;

    @Override
    public DeliveryDto createNewDelivery(DeliveryDto deliveryDto) {
        log.info("Обращение на PUT /api/v1/delivery");
        return deliveryService.createNewDelivery(deliveryDto);
    }

    @Override
    public void successfulDelivery(UUID orderId) {
        log.info("Обращение на POST /api/v1/delivery/successful");
        deliveryService.successfulDelivery(orderId);
    }

    @Override
    public void pickedDelivery(UUID orderId) {
        log.info("Обращение на POST /api/v1/delivery/picked");
        deliveryService.deliverPiked(orderId);

    }

    @Override
    public void failedDelivery(UUID orderId) {
        log.info("Обращение на POST /api/v1/delivery/failed");
        deliveryService.failedDelivery(orderId);
    }

    @Override
    public Double costDelivery(UUID orderId) {
        return 0.0;
    }
}

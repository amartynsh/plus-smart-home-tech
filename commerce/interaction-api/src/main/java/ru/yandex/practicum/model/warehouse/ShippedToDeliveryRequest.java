package ru.yandex.practicum.model.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ShippedToDeliveryRequest {
    @NotNull
    private UUID orderId;
    @NotNull
    private UUID deliveryId;
}

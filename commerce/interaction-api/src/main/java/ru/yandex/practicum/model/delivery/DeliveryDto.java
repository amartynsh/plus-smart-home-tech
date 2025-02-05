package ru.yandex.practicum.model.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.model.warehouse.AddressDto;

import java.util.UUID;

@Getter
@Setter
public class DeliveryDto {
    @NotNull
    UUID deliveryId;
    @NotNull
    AddressDto fromAddress;
    @NotNull
    AddressDto toAddress;
    @NotNull
    UUID orderId;
    @NotNull
    DeliveryState deliveryState;
}

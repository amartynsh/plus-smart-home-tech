package ru.yandex.practicum.model.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.model.warehouse.AddressDto;

import java.util.UUID;

@Builder
@Getter
@Setter
public class DeliveryDto {
    @NotNull
    private UUID deliveryId;
    @NotNull
    private AddressDto fromAddress;
    @NotNull
    private AddressDto toAddress;
    @NotNull
    private UUID orderId;
    @NotNull
    private DeliveryState deliveryState;
}

package ru.yandex.practicum.model.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookedProductsDto {
    @NotNull
    private double deliveryWeight;
    @NotNull
    private double deliveryVolume;
    @NotNull
    private boolean  fragile;
}

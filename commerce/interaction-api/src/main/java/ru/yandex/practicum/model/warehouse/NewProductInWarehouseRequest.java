package ru.yandex.practicum.model.warehouse;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NewProductInWarehouseRequest {
    private UUID productId;
    @Min(value = 1, message = "Вес должен быть больше 0")
    private Double weight;
    private DimensionDto dimension;;
    private Boolean fragile;
}

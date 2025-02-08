package ru.yandex.practicum.model.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NewProductInWarehouseRequest {
    @NotNull
    private UUID productId;
    @NotNull
    @Min(value = 1, message = "Вес должен быть больше 0")
    private Double weight;
    @NotNull
    private DimensionDto dimension;
    @NotNull
    private Boolean fragile;

    @Override
    public String toString() {
        return "NewProductInWarehouseRequest{" +
                "productId=" + productId +
                ", weight=" + weight +
                ", dimension=" + dimension +
                ", fragile=" + fragile +
                '}';
    }
}

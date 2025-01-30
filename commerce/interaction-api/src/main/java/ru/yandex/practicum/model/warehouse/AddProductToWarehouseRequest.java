package ru.yandex.practicum.model.warehouse;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddProductToWarehouseRequest {
    private UUID productId;
    @Min(value = 1, message = "Нельзя добавить товар менее чем 1шт")
    private Integer quantity;
}

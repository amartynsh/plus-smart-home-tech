package ru.yandex.practicum.model.warehouse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class AssemblyProductsForOrderRequest {
    @NotBlank
    private Map<UUID, Integer> products;
    @NotNull
    private UUID orderId;
}

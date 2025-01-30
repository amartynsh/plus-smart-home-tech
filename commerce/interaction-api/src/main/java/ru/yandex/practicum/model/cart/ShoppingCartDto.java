package ru.yandex.practicum.model.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
public class ShoppingCartDto {
    @NotNull
    private UUID shoppingCartId;
    @NotNull
    private Map<UUID, @Positive Integer> products;
}

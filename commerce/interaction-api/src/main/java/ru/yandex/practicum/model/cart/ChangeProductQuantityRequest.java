package ru.yandex.practicum.model.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@Builder
public class ChangeProductQuantityRequest {
    private UUID productId;
    private Long newQuantity;
}

package ru.yandex.practicum.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ProductReturnRequest {
    private UUID orderId;
    private Map<UUID, Integer> products;
}

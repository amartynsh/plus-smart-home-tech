package ru.yandex.practicum.model;

import java.util.Map;
import java.util.UUID;

public class ProductReturnRequest {
    UUID orderId;
    Map<UUID, Long> products;
}

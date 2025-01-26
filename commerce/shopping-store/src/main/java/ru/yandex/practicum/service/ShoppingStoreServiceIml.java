package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import ru.yandex.practicum.repository.ProductRepository;

@AllArgsConstructor
public class ShoppingStoreServiceIml {
    private final ProductRepository productRepository;

}

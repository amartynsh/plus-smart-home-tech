package ru.yandex.practicum.service;

import ru.yandex.practicum.model.cart.BookedProductsDto;
import ru.yandex.practicum.model.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.model.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addProduct(String username, Map<UUID, Long> products);

    void deactivateCart(String username);

    ShoppingCartDto removeProduct(String username, List<UUID> products);

    ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest);

    BookedProductsDto bookShoppingCartInWarehouse(String userName);

}

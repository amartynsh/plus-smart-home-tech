package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.clients.ShoppingCartClient;
import ru.yandex.practicum.model.ProductDto;
import ru.yandex.practicum.model.cart.BookedProductsDto;
import ru.yandex.practicum.model.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.model.cart.ShoppingCartDto;
import ru.yandex.practicum.service.ShoppingCartServiceImpl;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController implements ShoppingCartClient {
    private final ShoppingCartServiceImpl shoppingCartService;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    @Override
    public ShoppingCartDto addProductToCart(String username, Map<UUID, Long> products) {
        return shoppingCartService.addProduct(username, products);
    }

    @Override
    public void deactivateCart(String username) {
        shoppingCartService.deactivateCart(username);
    }

    @Override
    public ShoppingCartDto removeProductFromCart(String username, Map<UUID, Long> products) {
        return shoppingCartService.removeProduct(username, products);
    }

    @Override
    public ProductDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        return null;
    }

    @Override
    public BookedProductsDto bookProducts(String username) {
        return null;
    }
}

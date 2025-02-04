package ru.yandex.practicum.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.model.cart.ShoppingCartDto;
import ru.yandex.practicum.model.warehouse.AddressDto;

public class CreateNewOrderRequest {
    @NotBlank
    String username;
    @NotNull
    ShoppingCartDto shoppingCart;
    @NotNull
    AddressDto deliveryAddress;
}

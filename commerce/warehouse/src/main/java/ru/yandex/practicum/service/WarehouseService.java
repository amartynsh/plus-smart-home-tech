package ru.yandex.practicum.service;

import ru.yandex.practicum.model.cart.BookedProductsDto;
import ru.yandex.practicum.model.cart.ShoppingCartDto;
import ru.yandex.practicum.model.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.model.warehouse.AddressDto;
import ru.yandex.practicum.model.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {
    void addNewProduct(NewProductInWarehouseRequest request);

    BookedProductsDto checkProductQuantity(ShoppingCartDto shoppingCartDto);

    void addProductQuantity(AddProductToWarehouseRequest request);

    AddressDto getAddress();
}

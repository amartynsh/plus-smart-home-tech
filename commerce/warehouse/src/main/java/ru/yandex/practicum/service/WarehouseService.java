package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import ru.yandex.practicum.model.cart.BookedProductsDto;
import ru.yandex.practicum.model.cart.ShoppingCartDto;
import ru.yandex.practicum.model.warehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void addNewProduct(NewProductInWarehouseRequest request);

    BookedProductsDto checkProductQuantity(ShoppingCartDto shoppingCartDto);

    void addProductQuantity(AddProductToWarehouseRequest request);

    void returnProduct(Map<UUID, Integer> productList);

    AddressDto getAddress();

    @Transactional
    BookedProductsDto assembleOrder(AssemblyProductsForOrderRequest request);

    void shippedProduct(ShippedToDeliveryRequest request);
}

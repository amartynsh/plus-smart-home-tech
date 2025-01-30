package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.clients.WarehouseClient;
import ru.yandex.practicum.model.cart.BookedProductsDto;
import ru.yandex.practicum.model.cart.ShoppingCartDto;
import ru.yandex.practicum.model.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.model.warehouse.AddressDto;
import ru.yandex.practicum.model.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.service.WarehouseService;

@RestController
@Slf4j
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseClient {
    private final WarehouseService service;

    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        service.addNewProduct(request);

    }

    @Override
    public BookedProductsDto checkProductQuantity(ShoppingCartDto shoppingCartDto) {
        return service.checkProductQuantity(shoppingCartDto);
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        service.addProductQuantity(request);

    }

    @Override
    public AddressDto getAddress() {
        return service.getAddress();
    }
}

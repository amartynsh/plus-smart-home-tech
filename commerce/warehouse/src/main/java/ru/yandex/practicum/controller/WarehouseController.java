package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.clients.WarehouseClient;
import ru.yandex.practicum.model.cart.BookedProductsDto;
import ru.yandex.practicum.model.cart.ShoppingCartDto;
import ru.yandex.practicum.model.warehouse.*;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseClient {
    private final WarehouseService service;

    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        log.info("PUT /api/v1/warehouse, NewProductInWarehouseRequest: {}", request);
        service.addNewProduct(request);

    }

    @Override
    public BookedProductsDto checkProductQuantity(ShoppingCartDto shoppingCartDto) {
        log.info("POST /api/v1/warehouse/check, shoppingCartDto: {}", shoppingCartDto);
        return service.checkProductQuantity(shoppingCartDto);
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        log.info("POST /api/v1/warehouse/add, AddProductToWarehouseRequest: {}", request);
        service.addProductQuantity(request);
    }

    @Override
    public AddressDto getAddress() {
        log.info("GET /api/v1/warehouse/address");
        return service.getAddress();
    }

    @Override
    public void returnProductToWarehouse(Map<UUID, Integer> productList) {
        log.info("POST /api/v1/warehouse/return, productList: {}", productList);
        service.returnProduct(productList);
    }

    @Override
    public void shippedProduct(ShippedToDeliveryRequest request) {
        log.info("POST /api/v1/warehouse/shipped, ShippedToDeliveryRequest: {}", request);
        service.shippedProduct(request);
    }

    @Override
    public BookedProductsDto orderAssembly(AssemblyProductsForOrderRequest request) {
        log.info("POST /api/v1/warehouse/assembly, AssemblyProductsForOrderRequest: {}", request);
        return service.assembleOrder(request);
    }
}
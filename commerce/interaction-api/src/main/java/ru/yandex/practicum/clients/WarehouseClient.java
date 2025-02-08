package ru.yandex.practicum.clients;

import jakarta.validation.Valid;
import org.jetbrains.annotations.ApiStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.model.cart.BookedProductsDto;
import ru.yandex.practicum.model.cart.ShoppingCartDto;
import ru.yandex.practicum.model.warehouse.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient("warehouse")
public interface WarehouseClient {
    @PutMapping
    void addNewProduct(@RequestBody @Valid NewProductInWarehouseRequest request);

    @PostMapping("/check")
    BookedProductsDto checkProductQuantity(@RequestBody ShoppingCartDto shoppingCartDto);

    @PostMapping("/add")
    void addProduct(@RequestBody @Valid AddProductToWarehouseRequest request);

    @GetMapping("/address")
    AddressDto getAddress();

    @PostMapping("/return")
    void returnProductToWarehouse(@RequestBody @Valid Map<UUID, Integer> productList);

    @PostMapping("/shipped")
    void shippedProduct(@RequestBody @Valid ShippedToDeliveryRequest request);

    @PostMapping("/assembly")
    BookedProductsDto orderAssembly(@RequestBody AssemblyProductsForOrderRequest orderId);



}

package ru.yandex.practicum.clients;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.model.cart.BookedProductsDto;
import ru.yandex.practicum.model.cart.ShoppingCartDto;
import ru.yandex.practicum.model.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.model.warehouse.AddressDto;
import ru.yandex.practicum.model.warehouse.NewProductInWarehouseRequest;

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

}

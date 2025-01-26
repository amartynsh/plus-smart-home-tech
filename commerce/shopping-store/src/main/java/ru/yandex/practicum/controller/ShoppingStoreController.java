package ru.yandex.practicum.controller;

import clients.ShoppingStoreClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.Pageable;
import ru.yandex.practicum.model.ProductCategory;
import ru.yandex.practicum.model.ProductDto;
import ru.yandex.practicum.model.SetProductQuantityStateRequest;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/shopping-store")
public class ShoppingStoreController implements ShoppingStoreClient {


    @Override
    public List<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        return List.of();
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        return null;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        return null;
    }

    @Override
    public boolean removeProduct(UUID productId) {
        return false;
    }

    @Override
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        return false;
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        return null;
    }
}

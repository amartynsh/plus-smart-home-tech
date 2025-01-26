package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Pageable;
import ru.yandex.practicum.model.ProductCategory;
import ru.yandex.practicum.model.ProductDto;
import ru.yandex.practicum.model.SetProductQuantityStateRequest;

import java.util.List;
import java.util.UUID;

public interface ShoppingStoreService {
    List<ProductDto> getProducts(ProductCategory category, Pageable pageable);

    ProductDto saveProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean deleteProduct(UUID productId);

    boolean setProductQuantityState(SetProductQuantityStateRequest request);

    ProductDto getProduct(UUID productId);
}

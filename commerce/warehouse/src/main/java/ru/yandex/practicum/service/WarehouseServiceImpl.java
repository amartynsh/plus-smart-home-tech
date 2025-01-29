package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.errorhandler.exceptions.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.errorhandler.exceptions.NotFoundException;
import ru.yandex.practicum.errorhandler.exceptions.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.errorhandler.exceptions.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.WarehouseProductMapper;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.model.cart.BookedProductsDto;
import ru.yandex.practicum.model.cart.ShoppingCartDto;
import ru.yandex.practicum.model.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.model.warehouse.AddressDto;
import ru.yandex.practicum.model.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository repository;
    private final WarehouseProductMapper mapper;
    private final AddressDto warehouseAddress = initAddress();

    @Transactional
    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        log.info("Начал работать метод addNewProduct, на вход пришло NewProductInWarehouseRequest: {}", request);
        if (repository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Товар с таким productId:" +
                    request.getProductId() + " уже в наличии");
        }

        repository.save(mapper.toEntity(request));
    }

    @Transactional
    @Override
    public BookedProductsDto checkProductQuantity(ShoppingCartDto shoppingCartDto) {

        Set<UUID> requestProducts = shoppingCartDto.getProducts().keySet();
        List<WarehouseProduct> products = repository.findAllById(requestProducts);
        if (products.size() < requestProducts.size()) {
            throw new NotFoundException("Часть товара не найдена складе");
        }
        for (WarehouseProduct product : products) {
            if (product.getQuantity() < shoppingCartDto.getProducts().get(product.getProductId())) {
                 throw new ProductInShoppingCartLowQuantityInWarehouse("Товара с  productId:" +
                        product.getProductId() + " нет в достаточном количестве");
            }
        }
        double weightSum = 0;
        double deliveryVolumeSum = 0;
        boolean areThereAnyFragile = false;
        for (WarehouseProduct product : products) {
            weightSum += product.getWeight() * product.getQuantity();
            deliveryVolumeSum += product.getWeight() * product.getDepth() * product.getWidth() * product.getQuantity();
            if (product.isFragile()) {
                areThereAnyFragile = true;
            }
        }
        return BookedProductsDto.builder()
                .deliveryWeight(weightSum)
                .fragile(areThereAnyFragile)
                .deliveryVolume(deliveryVolumeSum)
                .build();
    }

    @Transactional
    @Override
    public void addProductQuantity(AddProductToWarehouseRequest request) {
        //
        if (!repository.existsById(request.getProductId())) {
            throw new NoSpecifiedProductInWarehouseException("Товар с таким productId:" + request.getProductId() + " не найден");
        }
        WarehouseProduct product = repository.findById(request.getProductId()).get();
        product.setQuantity(product.getQuantity() + request.getQuantity());
        repository.save(product);
    }

    @Override
    public AddressDto getAddress() {
        return warehouseAddress;
    }

    private AddressDto initAddress() {
        final String[] addresses = new String[]{"ADDRESS_1", "ADDRESS_2"};
        final String address = addresses[Random.from(new SecureRandom()).nextInt(0, 1)];
        return AddressDto.builder()
                .city(address)
                .street(address)
                .house(address)
                .country(address)
                .flat(address)
                .build();
    }
}
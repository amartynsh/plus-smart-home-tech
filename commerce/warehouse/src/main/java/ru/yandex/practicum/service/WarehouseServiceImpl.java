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
import ru.yandex.practicum.model.Booking;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.model.cart.BookedProductsDto;
import ru.yandex.practicum.model.cart.ShoppingCartDto;
import ru.yandex.practicum.model.warehouse.*;
import ru.yandex.practicum.repository.BookingRepository;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository productRepository;
    private final WarehouseProductMapper mapper;
    private final AddressDto warehouseAddress = initAddress();
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        log.info("Начал работать метод addNewProduct, на вход пришло NewProductInWarehouseRequest: {}", request);
        if (productRepository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Товар с таким productId:" +
                    request.getProductId() + " уже в наличии");
        }

        productRepository.save(mapper.toEntity(request));
    }

    @Transactional
    @Override
    public BookedProductsDto checkProductQuantity(ShoppingCartDto shoppingCartDto) {

        Set<UUID> requestProducts = shoppingCartDto.getProducts().keySet();
        List<WarehouseProduct> products = productRepository.findAllById(requestProducts);
        if (products.size() < requestProducts.size()) {
            throw new NotFoundException("Часть товара не найдена складе");
        }
        for (WarehouseProduct product : products) {
            if (product.getQuantity() < shoppingCartDto.getProducts().get(product.getProductId())) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Товара с  productId:" +
                        product.getProductId() + " нет в достаточном количестве");
            }
        }
        return makeBookedProductsDto(products);
    }

    @Transactional
    @Override
    public void addProductQuantity(AddProductToWarehouseRequest request) {
        //
        if (!productRepository.existsById(request.getProductId())) {
            throw new NoSpecifiedProductInWarehouseException("Товар с таким productId:" + request.getProductId() + " не найден");
        }
        WarehouseProduct product = productRepository.findById(request.getProductId()).get();
        product.setQuantity(product.getQuantity() + request.getQuantity());
        productRepository.save(product);
    }

    @Override
    public AddressDto getAddress() {
        return warehouseAddress;
    }

    @Transactional
    @Override
    public BookedProductsDto assembleOrder(AssemblyProductsForOrderRequest request) {
        List<WarehouseProduct> warehouseProductList = productRepository.findAllById(request.getProducts().keySet().stream().toList());
        if (warehouseProductList.size() < request.getProducts().size()) {
            throw new NotFoundException("Часть товара не найдена складе");
        }
        Booking newBooking = Booking.builder()
                .orderId(request.getOrderId())
                .products(request.getProducts())
                .build();
        bookingRepository.save(newBooking);

        for (WarehouseProduct product : warehouseProductList) {
            product.setQuantity(request.getProducts().get(product.getProductId()));
        }
        return makeBookedProductsDto(warehouseProductList);
    }

    @Override
    public void shippedProduct(ShippedToDeliveryRequest request) {
        log.info("Начал работать метод shippedProduct, на вход пришло ShippedToDeliveryRequest: {}", request);
        // НАДО ПОЛУЧИТЬ СУЩЕСТВУЮЩИЙ ЗАКАЗ И ОБНОВИТЬ ЕГО bookingRepository.()

        Optional<Booking> booking = bookingRepository.findByOrderId(request.getOrderId());
        if (booking.isEmpty()) {
            throw new NotFoundException("Заказ с таким orderId:" + request.getOrderId() + " не найден");
        }
        Booking updatedBooking = booking.get();
        updatedBooking.setDeliveryId(request.getDeliveryId());
        bookingRepository.save(updatedBooking);
    }

    @Transactional
    @Override
    public void returnProduct(Map<UUID, Integer> productList) {
        log.info("Начал работать метод returnProductToWarehouse, на вход пришло Map<UUID, Integer>: {}", productList);
        List<WarehouseProduct> savedProducts = productRepository.findAllById(productList.keySet());
        if (savedProducts.size() < productList.size()) {
            throw new NotFoundException("Часть товара не найдена складе");
        }
        Set<WarehouseProduct> updatedProducts = new HashSet<>();
        for (WarehouseProduct savedProduct : savedProducts) {
            Integer quantity = productList.get(savedProduct.getProductId());
            savedProduct.setQuantity(savedProduct.getQuantity() + quantity);
            updatedProducts.add(savedProduct);
        }
        log.info("сформировали список обновленных товаров с обновленным количеством товара: {}", updatedProducts);
        productRepository.saveAll(updatedProducts);
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

    private BookedProductsDto makeBookedProductsDto(List<WarehouseProduct> products) {
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
}
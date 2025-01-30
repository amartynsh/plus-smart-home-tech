package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.clients.WarehouseClient;
import ru.yandex.practicum.errorhandler.exceptions.NotFoundException;
import ru.yandex.practicum.errorhandler.exceptions.ValidationException;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.cart.BookedProductsDto;
import ru.yandex.practicum.model.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.model.cart.ShoppingCartDto;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        log.info("Начал работать метод getShoppingCart, на вход пришло username: {}", username);
        return shoppingCartMapper.toDto(shoppingCartRepository.findShoppingCartByUsername(username)
                .orElseThrow(() -> new NotFoundException("Корзины для пользователя " + username + " не существует")));
    }

    @Transactional
    @Override
    public ShoppingCartDto addProduct(String username, Map<UUID, Long> products) {
        log.info("Начал работать метод addProduct, на вход пришло username:{}, {}", username, products);
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUsername(username)
                .orElse(shoppingCartRepository.save(ShoppingCart.builder()
                        .username(username)
                        .build()));
        if (shoppingCart.isStatus()) {
            products.forEach((key, value) -> {
                shoppingCart.getProducts().put(key, value);
            });
            return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
        } else {
            throw new ValidationException("Корзина пользователя " + username + " не активна");
        }

    }

    @Override
    public void deactivateCart(String username) {
        log.info("Начал работать метод deactivateCart, на вход пришло username: {}", username);
        ShoppingCart shoppingCart = getCartInternal(username);
        shoppingCart.setStatus(false);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto removeProduct(String username, List<UUID> products) {
        log.info("Начал работать метод removeProduct, на вход пришло username:{}, products: {}", username, products);
        ShoppingCart shoppingCart = getCartInternal(username);
        products.forEach(key -> {
            if (!shoppingCart.getProducts().containsKey(key)) {
                throw new ValidationException("Нет искомых товаров в корзине");
            }
        });

        products.forEach(uuid -> {
            shoppingCart.getProducts().remove(uuid);
        });

        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Transactional
    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest) {
        log.info("Начал работать метод changeQuantity, на вход пришло username:{}, changeProductQuantityRequest: {}", username,
                changeProductQuantityRequest);
        ShoppingCart shoppingCart = getCartInternal(username);
        shoppingCart.getProducts().put(changeProductQuantityRequest.getProductId(), changeProductQuantityRequest.getNewQuantity());
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));

    }

    @Transactional
    @Override
    public BookedProductsDto bookShoppingCartInWarehouse(String userName) {
        log.info("Начал работать метод bookShoppingCartInWarehouse, на вход пришло userName: {}", userName);
        ShoppingCartDto cart = shoppingCartMapper.toDto(getCartInternal(userName));
        log.info("получили корзину: {}", cart);
        return warehouseClient.checkProductQuantity(cart);
    }


    private ShoppingCart getCartInternal(String username) {
        return shoppingCartRepository.findShoppingCartByUsername(username)
                .orElseThrow(() -> new NotFoundException("Корзины для пользователя " + username + " не существует"));
    }
}
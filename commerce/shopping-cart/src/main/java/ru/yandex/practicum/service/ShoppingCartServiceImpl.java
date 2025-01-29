package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.errorhandler.exceptions.NotFoundException;
import ru.yandex.practicum.errorhandler.exceptions.ValidationException;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.model.cart.ShoppingCartDto;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShoppingCartServiceImpl {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    public ShoppingCartDto getShoppingCart(String username) {
        log.info("Начал работать метод getShoppingCart, на вход пришло username: {}", username);
        return shoppingCartMapper.toDto(shoppingCartRepository.findShoppingCartByUsername(username)
                .orElseThrow(() -> new NotFoundException("Корзины для пользователя " + username + " не существует")));
    }

    @Transactional
    public ShoppingCartDto addProduct(String username, Map<UUID, Long> products) {
        log.info("Начал работать метод addProduct, на вход пришло username:{}, {}", username, products);
        ShoppingCart shoppingCart = getCartInternal(username);
        if (shoppingCart.isStatus()) {
            products.forEach((key, value) -> {
                shoppingCart.getProducts().put(key, value);
            });
            return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
        } else {
            throw new ValidationException("Корзина пользователя " + username + " не активна");
        }

    }

    public void deactivateCart(String username) {
        log.info("Начал работать метод deactivateCart, на вход пришло username: {}", username);
        ShoppingCart shoppingCart = getCartInternal(username);
        shoppingCart.setStatus(false);
        shoppingCartRepository.save(shoppingCart);
    }

    public ShoppingCartDto removeProduct(String username, Map<UUID, Long> products) {
        log.info("Начал работать метод removeProduct, на вход пришло username:{}, products: {}", username, products);
        ShoppingCart shoppingCart = getCartInternal(username);
        products.keySet().forEach(key -> {
            if (shoppingCart.getProducts().containsKey(key)) {
                throw new ValidationException("Нет искомых товаров в корзине");
            }
        });

        products.forEach((key, value) -> {
            shoppingCart.getProducts().remove(key);
        });

        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest) {
        log.info("Начал работать метод changeQuantity, на вход пришло username:{}, changeProductQuantityRequest: {}", username,
                changeProductQuantityRequest);
        ShoppingCart shoppingCart = getCartInternal(username);
        shoppingCart.getProducts().put(changeProductQuantityRequest.getProductId(), changeProductQuantityRequest.getNewQuantity());
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));

    }

    private ShoppingCart getCartInternal(String username) {
        return shoppingCartRepository.findShoppingCartByUsername(username)
                .orElseThrow(() -> new NotFoundException("Корзины для пользователя " + username + " не существует"));
    }
}
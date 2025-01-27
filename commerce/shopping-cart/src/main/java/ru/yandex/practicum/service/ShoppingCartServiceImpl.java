package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.cart.ShoppingCartDto;
import ru.yandex.practicum.repository.ShoppingCartRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShoppingCartServiceImpl {
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartDto getShoppingCart() {
        shoppingCartRepository.findByUsername(null);

    }



}

package ru.yandex.practicum.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.cart.BookedProductsDto;
import ru.yandex.practicum.model.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.model.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart")
public interface ShoppingCartClient {

    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam("username") String username);

    @PutMapping
    ShoppingCartDto addProductToCart(@RequestParam("username") String username,
                                     @RequestBody Map<UUID, Long> products);

    @DeleteMapping
    void deactivateCart(@RequestParam("username") String username);

    @PostMapping("/remove")
    ShoppingCartDto removeProductFromCart(@RequestParam("username") String username,
                                          @RequestBody List<UUID> products);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantity(@RequestParam("username") String username,
                                          @RequestBody ChangeProductQuantityRequest request);

    @PostMapping("/booking")
    BookedProductsDto bookProducts(@RequestParam("username") String username);
}
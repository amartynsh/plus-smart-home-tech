package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.cart.ShoppingCartDto;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShoppingCartMapper {

    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);
}

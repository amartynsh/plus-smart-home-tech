package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.model.delivery.DeliveryDto;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryDto toDto(Delivery delivery);

    Delivery toEntity(DeliveryDto deliveryDto);
}

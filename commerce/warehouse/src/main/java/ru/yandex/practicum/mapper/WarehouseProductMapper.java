package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.model.warehouse.NewProductInWarehouseRequest;

@Mapper(componentModel = "spring")
public interface WarehouseProductMapper {

    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "depth", source = "dto.dimension.depth")
    @Mapping(target = "width", source = "dto.dimension.width")
    @Mapping(target = "height", source = "dto.dimension.height")
    WarehouseProduct toEntity(NewProductInWarehouseRequest dto);

}

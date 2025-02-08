package ru.yandex.practicum.model.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DimensionDto {
    @NotNull
    private Double width;
    @NotNull
    private Double height;
    @NotNull
    private Double depth;
}
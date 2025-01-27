package ru.yandex.practicum.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ProductDto {
    private UUID productId;
    @NotBlank
    private  String productName;
    @NotBlank
    private  String description;
    private String imageSrc;
    @NotNull
    private  QuantityState quantityState;
    @NotNull
    private  ProductState productState;
    @Min(value = 1, message = "rating должен быть > 0")
    @Max(value = 5, message = "rating должен быть < 6")
    @NotNull
    private double rating;
    private ProductCategory productCategory;
    @NotNull
    @Min(value = 1, message = "price должен быть > 0")
    private double price;
}

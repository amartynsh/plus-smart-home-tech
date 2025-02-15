package ru.yandex.practicum.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Pageable {
    @NotNull
    @Min(value = 0, message = "Page must be at least 0")
    private int page;
    @NotNull
    @Min(value = 1, message = "Size must be at least 1")
    private int size;
    private List<String> sort;
}

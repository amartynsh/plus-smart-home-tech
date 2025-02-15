package ru.yandex.practicum.model;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SetProductQuantityStateRequest {
    private UUID productId;
    @NotNull
    private QuantityState quantityState;

}

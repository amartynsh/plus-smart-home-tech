package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Builder;

import java.util.UUID;
@Builder(toBuilder = true)
@Entity(name = "carts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "username")
    private String username;
    @Column(name = "status")
    private boolean status;

}

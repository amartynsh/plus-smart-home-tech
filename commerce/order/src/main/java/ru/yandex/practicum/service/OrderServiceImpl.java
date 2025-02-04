package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.CreateNewOrderRequest;
import ru.yandex.practicum.model.OrderDto;
import ru.yandex.practicum.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderServiceImpl {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> findBy(String username) {
        log.info("Вызван метод findBy username = {}", username);
        return orderRepository.findAllByUsername(username).stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    public OrderDto addOrder(CreateNewOrderRequest request) {
        log.info("Вызван метод addOrder, request = {}", request);
/*
TO DO
 */

        return null;
    }


}

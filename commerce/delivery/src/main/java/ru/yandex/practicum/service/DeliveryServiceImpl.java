package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.clients.OrderClient;
import ru.yandex.practicum.clients.WarehouseClient;
import ru.yandex.practicum.errorhandler.exceptions.NotFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.model.OrderDto;
import ru.yandex.practicum.model.delivery.DeliveryDto;
import ru.yandex.practicum.model.delivery.DeliveryState;
import ru.yandex.practicum.model.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final WarehouseClient warehouseClient;
    private final OrderClient orderClient;


    @Override
    public DeliveryDto createNewDelivery(DeliveryDto deliveryDto) {
        log.info("Вызван метод createNewDelivery, deliveryDto = {}", deliveryDto);
        Delivery delivery = deliveryMapper.toEntity(deliveryDto);
        delivery.setDeliveryState(DeliveryState.CREATED);
        return deliveryMapper.toDto(deliveryRepository.save(delivery));
    }

    @Override
    @Transactional
    public void successfulDelivery(UUID orderId) {
        log.info("Вызван метод successfulDelivery, orderId = {}", orderId);
        Delivery delivery = findByOrderId(orderId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        orderClient.orderDelivered(orderId);
        log.info("Успешная доставка для заказа {} сохранена", orderId);
    }

    @Transactional
    @Override
    public void failedDelivery(UUID orderId) {
        log.info("Вызван метод failedDelivery, orderId = {}", orderId);
        Delivery delivery = findByOrderId(orderId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        orderClient.orderDeliveryFailed(orderId);
        log.info("Неуспешная доставка для заказа {} сохранена", orderId);
    }

    @Transactional
    @Override
    public void deliverPiked(UUID orderId) {
        log.info("Вызван метод deliverPiked, orderId = {}", orderId);
        Delivery delivery = findByOrderId(orderId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);
        ShippedToDeliveryRequest request = ShippedToDeliveryRequest.builder()
                .deliveryId(delivery.getDeliveryId())
                .orderId(delivery.getOrderId())
                .build();

        warehouseClient.shippedProduct(request);
        log.info("Доставка для заказа {} помечена как захваченная", orderId);
    }

    @Transactional
    @Override
    public Double calcDeliveryPrice(OrderDto order) {
        log.info("Вызван метод calcDeliveryPrice, order = {}", order);
        Delivery delivery = findByOrderId(order.getOrderId());
        return DeliveryPriceCalculator.calculatePrice(delivery, order);
    }


    private Delivery findByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId).orElseThrow(() -> new NotFoundException("Доставка для заказа" +
                orderId + " не найдена"));
    }
}
package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.clients.DeliveryClient;
import ru.yandex.practicum.clients.PaymentClient;
import ru.yandex.practicum.clients.WarehouseClient;
import ru.yandex.practicum.errorhandler.exceptions.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.model.delivery.DeliveryDto;
import ru.yandex.practicum.model.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.repository.OrderRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WarehouseClient warehouseClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    @Override
    public List<OrderDto> findBy(String username) {
        log.info("Вызван метод findBy username = {}", username);
        return orderRepository.findAllByUsername(username).stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public OrderDto addNewOrder(CreateNewOrderRequest request) {
        log.info("Вызван метод addOrder, request = {}", request);
        Order order = Order.builder()
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .state(OrderState.NEW)
                .username(request.getUsername())
                .products(request.getShoppingCart().getProducts())
                .address(
                        Address.builder()
                                .country(request.getDeliveryAddress().getCountry())
                                .city(request.getDeliveryAddress().getCity())
                                .street(request.getDeliveryAddress().getStreet())
                                .flat(request.getDeliveryAddress().getFlat())
                                .build()
                )
                .build();
        order = orderRepository.save(order);
        DeliveryDto newDelivery = DeliveryDto.builder()
                .orderId(order.getOrderId())
                .toAddress(request.getDeliveryAddress())
                .build();
        deliveryClient.createNewDelivery(newDelivery);
        log.info("Создан заказ id = {}", order.getOrderId());
        return orderMapper.toDto(order);
    }

    @Transactional
    @Override
    public OrderDto returnProductToWarehouse(ProductReturnRequest request) {
        log.info("Вызван метод returnProductToWarehouse, request = {}", request);
        Order order = getOrder(request.getOrderId());
        warehouseClient.returnProductToWarehouse(request.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Transactional
    @Override
    public OrderDto setPaymentSuccess(UUID paymentId) {
        log.info("Вызван метод makePayment, paymentId = {}", paymentId);
        Order order = getOrder(paymentId);
        order.setState(OrderState.PAID);
        log.info("Для заказа с id = {}, установлено состояние = {}", order.getOrderId(), order.getState());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Transactional
    @Override
    public OrderDto setPaymentFailed(UUID paymentId) {
        log.info("Вызван метод setPaymentFailed, paymentId = {}", paymentId);
        Order order = getOrder(paymentId);
        order.setState(OrderState.PAYMENT_FAILED);
        log.info("Для заказа с id = {}, установлено состояние PAYMENT_FAILED", order.getOrderId());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Transactional
    @Override
    public OrderDto setOrderStateDelivered(UUID orderId) {
        log.info("Вызван метод setOrderStateDelivered, orderId = {}", orderId);
        Order order = getOrder(orderId);
        order.setState(OrderState.DELIVERED);
        log.info("Для заказа с id = {}, установлено состояние DELIVERED", order.getOrderId());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Transactional
    @Override
    public OrderDto setOrderDeliveryFailed(UUID orderId) {
        log.info("Вызван метод setOrderDeliveryFailed, orderId = {}", orderId);
        Order order = getOrder(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        log.info("Для заказа с id = {}, установлено состояние DELIVERY_FAILED", order.getOrderId());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Transactional
    @Override
    public OrderDto setOrderCompleted(UUID orderId) {
        log.info("Вызван метод setOrderCompleted, orderId = {}", orderId);
        Order order = getOrder(orderId);
        order.setState(OrderState.COMPLETED);
        log.info("Для заказа с id = {}, установлено состояние COMPLETED", order.getOrderId());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Transactional
    @Override
    public OrderDto getOrderTotalPrice(UUID orderId) {
        log.info("Вызван метод getOrderTotalPrice, orderId = {}", orderId);
        Order order = getOrder(orderId);
        Double totalPrice = paymentClient.calculateTotalCost(orderMapper.toDto(order));
        order.setTotalPrice(totalPrice);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Transactional
    @Override
    public OrderDto getOrderDeliveryPrice(UUID orderId) {
        log.info("Вызван метод getOrderDeliveryPrice, orderId = {}", orderId);
        Order order = getOrder(orderId);
        Double deliveryPrice = deliveryClient.costDelivery(orderId);
        order.setDeliveryPrice(deliveryPrice);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Transactional
    @Override
    public OrderDto orderAssembly(UUID orderId) {
        log.info("Вызван метод orderAssembly, orderId = {}", orderId);
        Order order = getOrder(orderId);
        AssemblyProductsForOrderRequest assemblyRequest = AssemblyProductsForOrderRequest.builder()
                .orderId(orderId)
                .products(order.getProducts())
                .build();
        warehouseClient.orderAssembly(assemblyRequest);
        order.setState(OrderState.ASSEMBLED);
        return orderMapper.toDto(orderRepository.save(order));
    }

    OrderDto assemblyFailed(UUID orderId) {
        log.info("Вызван метод assemblyFailed, orderId = {}", orderId);
        Order order = getOrder(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        return orderMapper.toDto(orderRepository.save(order));
    }

    private Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new NoSpecifiedProductInWarehouseException("Заказ не найден"));
    }

}

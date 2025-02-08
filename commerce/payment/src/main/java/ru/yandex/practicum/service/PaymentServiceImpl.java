package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.clients.DeliveryClient;
import ru.yandex.practicum.clients.OrderClient;
import ru.yandex.practicum.clients.ShoppingStoreClient;
import ru.yandex.practicum.errorhandler.exceptions.NoPaymentFoundException;
import ru.yandex.practicum.errorhandler.exceptions.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.OrderDto;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.model.PaymentState;
import ru.yandex.practicum.model.ProductDto;
import ru.yandex.practicum.model.payment.PaymentDto;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final ShoppingStoreClient shoppingStoreClient;
    private final DeliveryClient deliveryClient;
    private final Double TAX_RATE = 0.1;
    private final OrderClient orderClient;

    @Override
    public PaymentDto addNewPayment(OrderDto orderDto) {
        log.info("Вызван метод addNewPayment, orderDto = {}", orderDto);

        double productsCost = getProductsCost(orderDto);
        double deliveryPrice = getDeliveryPrice(orderDto);
        double taxValue = getTaxValue(productsCost);
        double totalCostPrice = productsCost + deliveryPrice;

        Payment newPayment = Payment.builder()
                .paymentState(PaymentState.PENDING)
                .feeTotal(taxValue)
                .deliveryPrice(deliveryPrice)
                .totalPayment(totalCostPrice)
                .orderId(orderDto.getOrderId())
                .build();
        return mapper.toDto(repository.save(newPayment));
    }

    @Transactional
    @Override
    public void paymentFailed(UUID orderId) {
        log.info("Вызван метод paymentFailed, orderDto = {}", orderId);
        Payment payment = getPayment(orderId);
        payment.setPaymentState(PaymentState.FAILED);
        orderClient.failedPayment(payment.getPaymentId());
    }

    @Override
    public void paymentSuccess(UUID orderId) {
        log.info("Вызван метод paymentSuccess, orderDto = {}", orderId);
        Payment payment = getPayment(orderId);
        payment.setPaymentState(PaymentState.SUCCESS);
        orderClient.payment(payment.getPaymentId());
    }

    private Double getProductsCost(OrderDto orderDto) {
        List<ProductDto> products = orderDto.getProducts().keySet()
                .stream()
                .map(shoppingStoreClient::getProduct)
                .toList();
        double productsCost = 0;
        if (products.size() != orderDto.getProducts().size()) {
            throw new NotEnoughInfoInOrderToCalculateException("Не хватает информации для расчета стоимости заказа");
        }
        for (ProductDto product : products) {
            productsCost += product.getPrice() * orderDto.getProducts().get(product.getProductId());
        }
        return productsCost;
    }

    private Double getDeliveryPrice(OrderDto orderDto) {
        return deliveryClient.costDelivery(orderDto.getOrderId());
    }

    private Double getTaxValue(double productsCost) {
        return TAX_RATE * productsCost;
    }

    @Override
    public double getTotalCostPrice(OrderDto orderDto) {
        double productsCost = getProductsCost(orderDto);
        double deliveryPrice = getDeliveryPrice(orderDto);
        return productsCost + deliveryPrice;
    }

    private Payment getPayment(UUID orderId) {
        return repository.findByOrderId(orderId).orElseThrow(
                () -> new NoPaymentFoundException("Платеж не найден")
        );
    }
}

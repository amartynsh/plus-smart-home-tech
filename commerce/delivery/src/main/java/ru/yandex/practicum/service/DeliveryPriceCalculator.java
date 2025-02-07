package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.model.OrderDto;

@Slf4j
public class DeliveryPriceCalculator {
    private static final Double basePrice = 5.0;
    private final static double address1PriceMod = 1.0;
    private final static double address2PriceMod = 2.0;
    private final static double fragilePriceMod = 0.3;
    private final static double wieghtPriceMod = 0.2;
    private final static double notSameAdressPriceMod = 0.2;
    private final static String ADDRESS_1 = "ADDRESS_1";
    private final static String ADDRESS_2 = "ADDRESS_2";

    public static Double calculatePrice(Delivery delivery, OrderDto orderDto) {
        log.info("Калькулятор стоимости доставки начал расчет цены");
        double price = basePrice;

        if (delivery.getFromAddress().toString().contains(ADDRESS_1)) {
            log.info("применили коэффициент adress1PriceMod = {}", address1PriceMod);
            price = price * address1PriceMod;
        }
        if (delivery.getFromAddress().toString().contains(ADDRESS_2)) {
            log.info("применили коэффициент address2PriceMod = {}", address2PriceMod);
            price = price * address2PriceMod;
        }
        if (orderDto.getFragile()) {
            log.info("применили коэффициент fragilePriceMod = {}", fragilePriceMod);
            price = price + (orderDto.getDeliveryWeight() * fragilePriceMod);
        }
        log.info("применили коэффициент wieghtPriceMod = {}", wieghtPriceMod);
        price = price + (orderDto.getDeliveryVolume() * wieghtPriceMod);

        if (!delivery.getFromAddress().getStreet().equals(delivery.getToAddress().getStreet())) {
            log.info("применили коэффициент notSameAdressPriceMod = {}", notSameAdressPriceMod);
            price = price * notSameAdressPriceMod;
        }
        log.info("Калькулятор вычислил цену доставки. Price = {}", price);
        return price;
    }
}

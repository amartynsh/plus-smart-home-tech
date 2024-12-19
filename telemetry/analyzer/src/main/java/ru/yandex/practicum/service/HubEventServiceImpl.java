package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.handler.hub.HubEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HubEventServiceImpl {
    private final Map<String, HubEventHandler> hubEventHandlers;
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public HubEventServiceImpl(Set<HubEventHandler> hubEventHandlers,
                               @GrpcClient("hub-router")HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient) {
        this.hubEventHandlers  = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getHubEventClassName,
                        Function.identity()
                ));
        this.hubRouterClient = hubRouterClient;
    }

    public void handleEvent(HubEventAvro eventAvro) {
        if (hubEventHandlers.containsKey(eventAvro.getPayload().getClass().getName())) {
            // если обработчик найден, передаём событие ему на обработку
            hubEventHandlers.get(eventAvro
                    .getPayload()
                    .getClass()
                    .getName())
                    .handle(eventAvro);
        } else {
            throw new IllegalArgumentException("Не могу найти обработчик для события " + eventAvro
                    .getPayload()
                    .getClass()
                    .getName());
        }
    }

}

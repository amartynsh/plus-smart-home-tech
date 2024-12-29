package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.handler.hub.HubEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HubEventServiceImpl implements HubEventService {
    private final Map<String, HubEventHandler> hubEventHandlers;


    public HubEventServiceImpl(Set<HubEventHandler> hubEventHandlers) {
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getHubEventClassName,
                        Function.identity()
                ));
    }

    @Override
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
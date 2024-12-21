package ru.yandex.practicum.handler.hub;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {
    String getHubEventClassName();

    void handle(HubEventAvro hubEventAvro);
}
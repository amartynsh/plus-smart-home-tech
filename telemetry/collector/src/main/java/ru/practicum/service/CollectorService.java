package ru.practicum.service;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface CollectorService {
    void sendSensorEvent(SensorEventAvro event);

    void sendHubEvent(HubEventAvro event);
}
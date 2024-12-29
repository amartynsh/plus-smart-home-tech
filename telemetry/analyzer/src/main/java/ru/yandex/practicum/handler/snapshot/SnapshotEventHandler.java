package ru.yandex.practicum.handler.snapshot;

import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.model.Condition;


public interface SnapshotEventHandler {
    String getType();

    Integer getSensorValue(Condition condition, SensorStateAvro sensorStateAvro);
}
package ru.yandex.practicum.mapper;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;

public class SnapshotMapper {
    public static SensorsSnapshotAvro EventToSnapshot(SensorEventAvro eventAvro) {
        return SensorsSnapshotAvro.newBuilder()
                .setHubId(eventAvro.getHubId())
                .setSensorsState(new HashMap<>())
                .setTimestamp(eventAvro.getTimestamp())
                .build();
    }

    public static SensorStateAvro EventToState(SensorEventAvro event) {
        return SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
    }
}

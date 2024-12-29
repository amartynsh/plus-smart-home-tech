package ru.yandex.practicum.deserializer;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class SnapshotEventDeserializer extends BaseAvroDeserializer<SensorsSnapshotAvro> {

    public SnapshotEventDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }
}

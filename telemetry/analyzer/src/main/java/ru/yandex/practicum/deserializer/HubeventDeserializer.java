package ru.yandex.practicum.deserializer;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public class HubeventDeserializer extends BaseAvroDeserializer<SensorEventAvro> {
    public HubeventDeserializer() {
        super(HubEventAvro.getClassSchema());
    }

}
package ru.yandex.practicum.handler.snapshot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.ConditionType;

@Component
@Slf4j
public class LightSensorEventHandler implements SnapshotEventHandler {
    @Override
    public String getType() {
        return LightSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(Condition condition, SensorStateAvro sensorStateAvro) {
        log.info("LightSensorEventHandler: Начал обрабатывать запрос");
        LightSensorAvro lightSensorAvro = (LightSensorAvro) sensorStateAvro.getData();
        return switch (condition.getType()) {
            case ConditionType.LUMINOSITY -> lightSensorAvro.getLuminosity();
            default -> {
                log.info("LightSensorEventHandler: Произошла ошибка");
                yield null;
            }
        };
    }
}
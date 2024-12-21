package ru.yandex.practicum.handler.snapshot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.ConditionType;

@Component
@Slf4j
public class TemperatureSensorEventHandler implements SnapshotEventHandler {

    @Override
    public String getType() {
        return TemperatureSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(Condition condition, SensorStateAvro sensorStateAvro) {
        TemperatureSensorAvro temperatureSensorAvro = (TemperatureSensorAvro) sensorStateAvro.getData();
        log.info("TemperatureSensorEventHandler: Начал обрабатывать запрос");
        return switch (condition.getType()) {
            case ConditionType.TEMPERATURE -> temperatureSensorAvro.getTemperatureC();
            default -> {
                log.info("TemperatureSensorEventHandler: Произошла ошибка");
                yield null;
            }
        };
    }
}
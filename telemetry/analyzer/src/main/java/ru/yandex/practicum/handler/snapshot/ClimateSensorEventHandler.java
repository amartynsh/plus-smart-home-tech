package ru.yandex.practicum.handler.snapshot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.model.Condition;

@Component
@Slf4j
public class ClimateSensorEventHandler implements SnapshotEventHandler {

    @Override
    public String getType() {
        return ClimateSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(Condition condition, SensorStateAvro sensorStateAvro) {
        log.info("ClimateSensorEventHandler: Начал обрабатывать запрос");
        ClimateSensorAvro climateSensorAvro = (ClimateSensorAvro) sensorStateAvro.getData();
        return switch (condition.getType()) {
            case HUMIDITY -> climateSensorAvro.getHumidity();
            case CO2LEVEL -> climateSensorAvro.getCo2Level();
            case TEMPERATURE -> climateSensorAvro.getTemperatureC();
            default -> {
                log.info("ClimateSensorEventHandler: Произошла ошибка");
                yield null;
            }
        };
    }
}
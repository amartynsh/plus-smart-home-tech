package ru.yandex.practicum.handler.snapshot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.ConditionType;

@Slf4j
@Component
public class SwitchSensorEventHandler implements SnapshotEventHandler {

    @Override
    public String getType() {
        return SwitchSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(Condition condition, SensorStateAvro sensorStateAvro) {
        log.info("Обработчик SwitchSensor: начал работу");
        SwitchSensorAvro switchSensorAvro = (SwitchSensorAvro) sensorStateAvro.getData();
        Integer result = switch (condition.getType()) {
            case ConditionType.SWITCH -> switchSensorAvro.getState() ? 1 : 0;
            default -> null;
        };
        log.info("Обработчик SwitchSensor: Значение result {}", result);
        return result;
    }
}
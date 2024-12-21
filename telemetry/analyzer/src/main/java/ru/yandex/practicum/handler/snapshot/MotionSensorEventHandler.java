package ru.yandex.practicum.handler.snapshot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.ConditionType;

@Slf4j
@Component
public class MotionSensorEventHandler implements SnapshotEventHandler {
    @Override
    public String getType() {
        return MotionSensorAvro.class.getName();
    }

    @Override
    public Integer getSensorValue(Condition condition, SensorStateAvro sensorStateAvro) {
        log.info("MotionSensorEventHandler: Начал обрабатывать запрос");
        MotionSensorAvro motionSensorAvro = (MotionSensorAvro) sensorStateAvro.getData();
        return switch (condition.getType()) {
            case ConditionType.MOTION -> motionSensorAvro.getMotion() ? 1 : 0;
            default -> {
                log.info("MotionSensorEventHandler: Произошла ошибка");
                yield null;
            }
        };
    }
}
package ru.practicum.mapper.proto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.service.CollectorService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

import java.time.Instant;

@Slf4j
@Component
public class SwitchSensorEventHandler implements SensorEventHandler {
    CollectorService collectorService;

    public SwitchSensorEventHandler(CollectorService collectorService) {
        this.collectorService = collectorService;
    }


    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto eventProto) {
        SensorEventAvro eventAvro = new SensorEventAvro();
        SwitchSensorAvro switchSensorEvent = new SwitchSensorAvro();
        switchSensorEvent.setState(eventProto.getSwitchSensorEvent().getState());

        eventAvro.setId(eventProto.getId());
        eventAvro.setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                eventProto.getTimestamp().getNanos()));
        eventAvro.setHubId(eventProto.getHubId());
        eventAvro.setPayload(switchSensorEvent);
        collectorService.sendSensorEvent(eventAvro);
    }
}
package ru.practicum.handlers.sensor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ru.practicum.service.CollectorService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Slf4j
@Component
public class MotionSensorEventHandler implements SensorEventHandler {
    CollectorService collectorService;

    public MotionSensorEventHandler(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto eventProto) {
        SensorEventAvro eventAvro = new SensorEventAvro();
        MotionSensorAvro motionSensorEvent = new MotionSensorAvro();
        motionSensorEvent.setMotion(eventProto.getMotionSensorEvent().getMotion());
        motionSensorEvent.setLinkQuality(eventProto.getMotionSensorEvent().getLinkQuality());
        motionSensorEvent.setVoltage(eventProto.getMotionSensorEvent().getVoltage());

        eventAvro.setId(eventProto.getId());
        eventAvro.setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                eventProto.getTimestamp().getNanos()));
        eventAvro.setHubId(eventProto.getHubId());
        eventAvro.setPayload(motionSensorEvent);

        collectorService.sendSensorEvent(eventAvro);
    }
}
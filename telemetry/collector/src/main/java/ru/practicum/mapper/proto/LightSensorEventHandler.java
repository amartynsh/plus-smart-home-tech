package ru.practicum.mapper.proto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.service.CollectorService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Slf4j
@Component
public class LightSensorEventHandler implements SensorEventHandler {

    CollectorService collectorService;

    public LightSensorEventHandler(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto eventProto) {
        SensorEventAvro eventAvro = new SensorEventAvro();
        LightSensorAvro lightSensorAvro = new LightSensorAvro();
        lightSensorAvro.setLuminosity(eventProto.getLightSensorEvent().getLuminosity());
        lightSensorAvro.setLinkQuality(eventProto.getLightSensorEvent().getLinkQuality());

        eventAvro.setId(eventProto.getId());
        eventAvro.setHubId(eventProto.getHubId());
        eventAvro.setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                eventProto.getTimestamp().getNanos()));
        eventAvro.setPayload(lightSensorAvro);

        log.info("Собрали событие {}", eventAvro);
        collectorService.sendSensorEvent(eventAvro);
    }
}
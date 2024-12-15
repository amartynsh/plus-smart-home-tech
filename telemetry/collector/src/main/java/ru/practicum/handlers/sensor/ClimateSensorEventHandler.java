package ru.practicum.handlers.sensor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.service.CollectorService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Slf4j
@Component
public class ClimateSensorEventHandler implements SensorEventHandler {
    CollectorService collectorService;

    public ClimateSensorEventHandler(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto eventProto) {

        SensorEventAvro eventAvro = new SensorEventAvro();
        ClimateSensorAvro climateSensorAvroAvro = new ClimateSensorAvro();
        climateSensorAvroAvro.setCo2Level(eventProto.getClimateSensorEvent().getCo2Level());
        climateSensorAvroAvro.setHumidity(eventProto.getClimateSensorEvent().getHumidity());
        climateSensorAvroAvro.setTemperatureC(eventProto.getClimateSensorEvent().getTemperatureC());

        eventAvro.setId(eventProto.getId());
        eventAvro.setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                eventProto.getTimestamp().getNanos()));
        eventAvro.setHubId(eventProto.getHubId());
        eventAvro.setPayload(climateSensorAvroAvro);

        collectorService.sendSensorEvent(eventAvro);
    }
}
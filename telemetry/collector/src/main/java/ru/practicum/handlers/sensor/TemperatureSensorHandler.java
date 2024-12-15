package ru.practicum.handlers.sensor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.service.CollectorService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.time.Instant;

@Slf4j
@Component
public class TemperatureSensorHandler implements SensorEventHandler {

    CollectorService collectorService;

    public TemperatureSensorHandler(CollectorService collectorService) {
        this.collectorService = collectorService;
    }


    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto eventProto) {
        SensorEventAvro eventAvro = new SensorEventAvro();

        TemperatureSensorAvro temperatureSensorAvroAvro = new TemperatureSensorAvro();
        temperatureSensorAvroAvro.setTemperatureC(eventProto.getTemperatureSensorEvent().getTemperatureC());
        temperatureSensorAvroAvro.setTemperatureF(eventProto.getTemperatureSensorEvent().getTemperatureF());

        eventAvro.setId(eventProto.getId());
        eventAvro.setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                eventProto.getTimestamp().getNanos()));
        eventAvro.setHubId(eventProto.getHubId());
        eventAvro.setPayload(temperatureSensorAvroAvro);

        collectorService.sendSensorEvent(eventAvro);
    }
}
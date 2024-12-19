package ru.yandex.practicum.handler.hub;


import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.SensorRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeviceAddedEventHandler implements HubEventHandler {
    private final SensorRepository sensorRepository;

    @Override
    public String getHubEventClassName() {
        return DeviceAddedEventAvro.class.getName();
    }

    @Override
    public void handle(HubEventAvro hubEventAvro) {

        Sensor sensor = new Sensor();
        sensor.setHubId(hubEventAvro.getHubId());
        sensor.setId(((DeviceAddedEventAvro) hubEventAvro.getPayload()).getId());
        log.info("Сенсор: {}", sensor.toString());
        sensorRepository.save(sensor);
    }
}

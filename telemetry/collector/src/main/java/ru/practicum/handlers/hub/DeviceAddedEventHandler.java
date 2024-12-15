package ru.practicum.handlers.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.service.CollectorService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Slf4j
@Component
public class DeviceAddedEventHandler implements HubEventHandler {

    CollectorService collectorService;

    public DeviceAddedEventHandler(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEventProto eventProto) {
        log.info("Обработчик {} начал работать", getClass());
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                        eventProto.getTimestamp().getNanos()))
                .setPayload(DeviceAddedEventAvro.newBuilder()
                        .setType(DeviceTypeAvro.valueOf(eventProto.getDeviceAdded().getType().toString()))
                        .setId(eventProto.getDeviceAdded().getId())
                        .build())
                .build();
        log.info("Смапили событие  в AVRO {}", eventAvro.toString());
        collectorService.sendHubEvent(eventAvro);
    }
}

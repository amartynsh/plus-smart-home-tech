package ru.practicum.handlers.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.service.CollectorService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;


@Slf4j
@Component
public class DeviceRemovedEventHandler implements HubEventHandler {
    CollectorService collectorService;

    public DeviceRemovedEventHandler(CollectorService collectorService) {
        this.collectorService = collectorService;
    }


    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEventProto eventProto) {
        log.info("Обработчик {} начал работать", getClass());
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                        eventProto.getTimestamp().getNanos()))
                .setPayload(DeviceRemovedEventAvro.newBuilder()
                        .setId(eventProto.getDeviceRemoved().getId())
                        .build())
                .build();
        log.info("Смапили событие  в AVRO {}", eventAvro.toString());
        collectorService.sendHubEvent(eventAvro);

    }
}

package ru.practicum.handlers.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.service.CollectorService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.time.Instant;

@Slf4j
@Component
public class ScenarioRemovedEventHandler implements HubEventHandler {
    CollectorService collectorService;

    public ScenarioRemovedEventHandler(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEventProto eventProto) {
        log.info("Обработчик {} начал работать", getClass());
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                        eventProto.getTimestamp().getNanos()))
                .setPayload(ScenarioRemovedEventAvro.newBuilder()
                        .setName(eventProto.getScenarioRemoved().getName())
                        .build())
                .build();
        log.info("Смапили событие  в AVRO {}", eventAvro.toString());
        collectorService.sendHubEvent(eventAvro);
    }
}
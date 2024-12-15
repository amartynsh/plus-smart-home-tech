package ru.practicum.handlers.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.service.CollectorService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ScenarioAddedEventHandler implements HubEventHandler {
    CollectorService collectorService;

    public ScenarioAddedEventHandler(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEventProto eventProto) {

        log.info("Обработчик {} начал работать", getClass());
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                        eventProto.getTimestamp().getNanos()))
                .setPayload(ScenarioAddedEventAvro.newBuilder()
                        .setActions(eventProto.getScenarioAdded().getActionList().stream()
                                .map(action -> DeviceActionAvro.newBuilder()
                                        .setSensorId(action.getSensorId())
                                        .setType(ActionTypeAvro.valueOf(action.getType().toString()))
                                        .setValue(action.getValue())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .build();
        log.info("Смапили событие  в AVRO {}", eventAvro.toString());
        collectorService.sendHubEvent(eventAvro);
    }
}


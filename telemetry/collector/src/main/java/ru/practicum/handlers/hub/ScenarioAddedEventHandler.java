package ru.practicum.handlers.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.service.CollectorService;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

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
        log.info("На вход:{}", eventProto.toString());
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(Instant.ofEpochSecond(eventProto.getTimestamp().getSeconds(),
                        eventProto.getTimestamp().getNanos()))

                .setPayload(ScenarioAddedEventAvro.newBuilder()
                        .setName(eventProto.getScenarioAdded().getName())
                        .setActions(eventProto.getScenarioAdded().getActionList().stream()
                                .map(this::actionToAvro)
                                .collect(Collectors.toList()))
                        .setConditions(eventProto.getScenarioAdded().getConditionList().stream()
                                .map(this::conditionToAvro)
                                .collect(Collectors.toList()))
                        .build())
                .build();


        log.info("Смапили событие  в AVRO {}", eventAvro.toString());
        collectorService.sendHubEvent(eventAvro);
    }

    private DeviceActionAvro actionToAvro(DeviceActionProto actionProto) {
        log.info("Началить делать deviceActionAvro из {}", actionProto);
        return DeviceActionAvro.newBuilder()
                .setSensorId(actionProto.getSensorId())
                .setType(ActionTypeAvro.valueOf(actionProto.getType().name()))
                .setValue(actionProto.getValue())
                .build();
    }

    private ScenarioConditionAvro conditionToAvro(ScenarioConditionProto conditionProto) {
        log.info("Началить делать conditionToAvro из {}", conditionProto);
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(conditionProto.getSensorId())
                .setType(ConditionTypeAvro.valueOf(conditionProto.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(conditionProto.getOperation().name()))
                .setValue(conditionProto.getIntValue())
                .build();
    }

}


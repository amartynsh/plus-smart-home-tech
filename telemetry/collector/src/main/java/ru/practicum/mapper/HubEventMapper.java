package ru.practicum.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.model.hub.*;
import ru.practicum.model.hub.constants.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.stream.Collectors;

@Slf4j
public class HubEventMapper {

    public static HubEventAvro map(HubEvent event) {
        log.info("Начали маппинг");
        switch (event.getType()) {
            case HubEventType.DEVICE_ADDED:
                log.info("Начали маппинг DEVICE_ADDED");
                DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) event;
                return HubEventAvro.newBuilder()
                        .setHubId(deviceAddedEvent.getHubId())
                        .setTimestamp(deviceAddedEvent.getTimestamp())
                        .setPayload(DeviceAddedEventAvro.newBuilder()
                                .setId(deviceAddedEvent.getId())
                                .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().toString()))
                                .build())
                        .build();

            case HubEventType.DEVICE_REMOVED:
                log.info("Начали маппинг DEVICE_REMOVED");
                DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) event;
                return HubEventAvro.newBuilder()
                        .setHubId(deviceRemovedEvent.getHubId())
                        .setTimestamp(deviceRemovedEvent.getTimestamp())
                        .setPayload(DeviceRemovedEventAvro.newBuilder()
                                .setId(deviceRemovedEvent.getId())
                                .build())
                        .build();

            case SCENARIO_ADDED:
                log.info("Начали маппинг SCENARIO_ADDED");
                ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;
                return HubEventAvro.newBuilder()
                        .setHubId(scenarioAddedEvent.getHubId())
                        .setTimestamp(scenarioAddedEvent.getTimestamp())
                        .setPayload(ScenarioAddedEventAvro.newBuilder()
                                .setActions(scenarioAddedEvent.getActions().stream()
                                        .map(action -> DeviceActionAvro.newBuilder()
                                                .setSensorId(action.getSensorId())
                                                .setType(ActionTypeAvro.valueOf(action.getType().toString()))
                                                .setValue(action.getValue())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                        .build();

            case SCENARIO_REMOVED:
                log.info("Начали маппинг SCENARIO_REMOVED");
                ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;
                return HubEventAvro.newBuilder()
                        .setHubId(scenarioRemovedEvent.getHubId())
                        .setTimestamp(scenarioRemovedEvent.getTimestamp())
                        .setPayload(ScenarioRemovedEventAvro.newBuilder()
                                .setName(scenarioRemovedEvent.getName())
                                .build())
                        .build();
            default:
                throw new IllegalArgumentException("Unknown event type");
        }

    }
}

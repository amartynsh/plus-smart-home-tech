package ru.yandex.practicum.service;

import com.google.protobuf.Timestamp;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.handler.snapshot.SnapshotEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.ConditionOperation;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SnapshotServiceImpl implements SnapshotService {

    private final ScenarioRepository scenarioRepository;
    private final Map<String, SnapshotEventHandler> snapshotEventHandlerMap;
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public SnapshotServiceImpl(ScenarioRepository scenarioRepository, Set<SnapshotEventHandler> snapshotEventHandlerMap,
                               @GrpcClient("hub-router") HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient) {
        this.scenarioRepository = scenarioRepository;
        this.snapshotEventHandlerMap = snapshotEventHandlerMap.stream()
                .collect(Collectors.toMap(SnapshotEventHandler::getType,
                        Function.identity()
                ));
        this.hubRouterClient = hubRouterClient;
    }

    @Override
    public void handleSnapshot(SensorsSnapshotAvro sensorsSnapshotAvro) {
        log.info("сервис SnapshotServiceImpl начал обработку SensorsSnapshotAvro {}", sensorsSnapshotAvro);
        log.info("Список обработчиков {}", snapshotEventHandlerMap);
        List<Scenario> scenarios = scenarioRepository.findByHubId(sensorsSnapshotAvro.getHubId());
        Map<String, SensorStateAvro> sensorStates = sensorsSnapshotAvro.getSensorsState();
        log.info("Значение SensorStateAvro keys{}, values {}", sensorStates.keySet(), sensorStates.values());

        List<Scenario> scenariosToSend = scenarios.stream()
                .filter(scenario -> validateConditions(scenario.getConditions(), sensorStates))
                .toList();

        log.info("Список сценариев для отправки scenariosToSend {}", scenariosToSend);
        List<DeviceActionRequest> deviceActionRequests = getDeviceActionListToSend(scenariosToSend);
        sendAction(deviceActionRequests);


    }

    private Boolean validateConditions(List<Condition> conditions, Map<String, SensorStateAvro> sensorStateAvros) {
        log.info("В метода validateCond пришло sensorStateAvros{}", sensorStateAvros);
        log.info("В метода validateCond пришло List<Condition> conditions {}", conditions);
        return conditions.stream()
                .allMatch(condition -> checkConditionValidity(condition, sensorStateAvros.get(condition.getSensor().getId())));
    }

    private Boolean checkConditionValidity(Condition condition, SensorStateAvro sensorStateAvro) {
        log.info("В checkConditionValidity пришло значение sensorStateAvro {}", sensorStateAvro);
        log.info("Значение condition {}", condition);
        Integer sensorValue = snapshotEventHandlerMap
                .get(sensorStateAvro.getData().getClass().getName())
                .getSensorValue(condition, sensorStateAvro);
        log.info("Значение value {}", sensorValue);

        return switch (condition.getOperation()) {
            case ConditionOperation.LOWER_THAN -> sensorValue < condition.getValue();
            case ConditionOperation.EQUALS -> sensorValue.equals(condition.getValue());
            case ConditionOperation.GREATER_THAN -> sensorValue > condition.getValue();
        };
    }

    private void sendAction(List<DeviceActionRequest> deviceActionRequests) {
        log.info("sendAction: Начали обработку");
        if (!deviceActionRequests.isEmpty()) {
            log.info("sendAction: Лист событий на отправку не пустой");
            for (DeviceActionRequest deviceActionRequest : deviceActionRequests) {
                try {
                    hubRouterClient.handleDeviceAction(deviceActionRequest);
                    log.info("Отправили DeviceActionRequest{}", deviceActionRequest);
                } catch (StatusRuntimeException e) {
                    log.info("Сервер gRPC недоступен");
                }
            }

        } else {
            log.info("sendAction: Нечего оптравлять, список deviceActionRequests пуст ");
        }
    }

    private List<DeviceActionRequest> getDeviceActionListToSend(List<Scenario> scenarios) {
        List<DeviceActionRequest> deviceActionRequests = new ArrayList<>();
        log.info("getDeviceActionListToSend: Начали обработку");
        for (Scenario scenario : scenarios) {
            log.info("getDeviceActionListToSend: Начали обработку Scenario {}", scenario);
            if (scenario.getActions() != null) {
                List<Action> actionList = scenario.getActions();
                for (Action action : actionList) {
                    log.info("getDeviceActionListToSend: Начали обработку actionList");
                    Instant instant = Instant.now();
                    DeviceActionProto deviceActionProto = DeviceActionProto.newBuilder()
                            .setSensorId(action.getSensor().getId())
                            .setType(ActionTypeProto.valueOf(action.getType().name()))
                            .setValue(action.getValue())
                            .build();
                    DeviceActionRequest deviceActionRequest = DeviceActionRequest.newBuilder()
                            .setHubId(action.getScenario().getHubId())
                            .setScenarioName(action.getScenario().getName())
                            .setTimestamp(Timestamp.newBuilder()
                                    .setNanos(instant.getNano())
                                    .setSeconds(instant.getEpochSecond())
                                    .build())
                            .setAction(deviceActionProto)
                            .build();
                    log.info("Сформировали DeviceActionRequest {}", deviceActionRequest);
                    deviceActionRequests.add(deviceActionRequest);
                }
            }
        }
        return deviceActionRequests;
    }
}

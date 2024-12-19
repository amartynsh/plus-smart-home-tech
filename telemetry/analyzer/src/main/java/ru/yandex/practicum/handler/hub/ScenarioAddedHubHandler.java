package ru.yandex.practicum.handler.hub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.ActionRepository;
import ru.yandex.practicum.repository.ConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.repository.SensorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Component
public class ScenarioAddedHubHandler implements HubEventHandler {
    private final SensorRepository sensorRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final ScenarioRepository scenarioRepository;


    @Override
    public String getHubEventClassName() {
        return ScenarioAddedEventAvro.class.getName();
    }

    @Override
    public void handle(HubEventAvro hubEventAvro) {
        log.info("Начали обработку hubEventAvro {}", hubEventAvro);
        ScenarioAddedEventAvro scenarioAddedEventAvro = (ScenarioAddedEventAvro) hubEventAvro.getPayload();
       Optional<Scenario> scenarioOld = scenarioRepository.findByHubIdAndName(hubEventAvro.getHubId(),
                scenarioAddedEventAvro.getName());

        Scenario scenario = new Scenario();

 /*       try {
            checkHubEvent(hubEventAvro);
        } catch (RuntimeException e) {
            throw new RuntimeException("Не найдены сенсоры из запроса!");
        }*/
        if (!scenarioOld.isPresent()) {
            log.info("Сценария не существует, поэтому создаем новый");
            scenario = avroToScenarioEntity(hubEventAvro, scenarioAddedEventAvro);
            log.info("Создали сценарий {}", scenario);

        } /*else {
            scenario = scenarioOld.get();
            //Чистим старые условия
            if (scenario.getConditions() != null) {
                log.info("old Conditions: {}", scenarioOld.get().getConditions());
                conditionRepository.deleteAllById(scenarioOld.get().getConditions().stream().map(Condition::getId).toList());

            }
            Scenario finalScenario = scenario;
            List<Condition> conditions = new ArrayList<>(scenarioAddedEventAvro.getConditions().stream()
                    .map(conditionAvro -> avroToConditionEntity(finalScenario, conditionAvro))
                    .toList());
            //Удаляем старые условия и действия
            if (scenario.getActions() != null) {
                actionRepository.deleteAllById(scenarioOld.get().getActions().stream().map(Action::getId).toList());
            }
            //Добавляем новые
            scenarioOld.get().setConditions(conditions);

            List<Action> actions = new ArrayList<>(scenarioAddedEventAvro.getActions().stream()
                    .map(actionAvro -> toActionEntity(finalScenario, actionAvro))
                    .toList());

            scenarioOld.get().setActions(actions);
        }*/
        log.info("Сценарий для сохранения: {}", scenario.toString());

        scenarioRepository.save(scenario);

    }

    private Scenario avroToScenarioEntity(HubEventAvro hubEventAvro, ScenarioAddedEventAvro scenarioAddedEventAvro) {
        Scenario scenario = new Scenario();

        scenario.setHubId((hubEventAvro.getHubId()));
        scenario.setName(scenarioAddedEventAvro.getName());
        scenario.setConditions(scenarioAddedEventAvro.getConditions()
                .stream()
                .map(scenarioConditionAvro -> avroToConditionEntity(scenario, scenarioConditionAvro))
                .toList());
        log.info("Замапили сценарий {}", scenario);
        return scenario;
    }


    private Condition avroToConditionEntity(Scenario scenario, ScenarioConditionAvro scenarioConditionAvro) {
        log.info("Проверка на петлю");
        return Condition.builder()
                .sensor(new Sensor(scenarioConditionAvro.getSensorId(), scenario.getHubId()))
                .type(avroToConditioTypeEntity(scenarioConditionAvro.getType()))
                .operation(avroToConditionOperationEntity(scenarioConditionAvro.getOperation()))
                .value(getConditionValue(scenarioConditionAvro))
                .scenarios(List.of(scenario))
                .build();
    }

    private ConditionType avroToConditioTypeEntity(ConditionTypeAvro conditionTypeAvro) {
        return ConditionType.valueOf(conditionTypeAvro.name());
    }

    private ConditionOperation avroToConditionOperationEntity(ConditionOperationAvro conditionOperationAvro) {
        return ConditionOperation.valueOf(conditionOperationAvro.name());
    }

    private Integer getConditionValue(ScenarioConditionAvro scenarioConditionAvro) {
        if (scenarioConditionAvro.getValue() instanceof Boolean) {
            if ((Boolean) scenarioConditionAvro.getValue()) {
                return 1;
            } else return 0;
        }
        if (scenarioConditionAvro.getValue() instanceof Integer) {
            return (Integer) scenarioConditionAvro.getValue();
        }
        return null;
    }

    public Action toActionEntity(Scenario scenario, DeviceActionAvro deviceActionAvro) {
        return Action.builder()
                .sensor(new Sensor(deviceActionAvro.getSensorId(), scenario.getHubId()))
                .type(toActionTypeEntity(deviceActionAvro.getType()))
                .value(deviceActionAvro.getValue())
                .build();
    }

    public ActionType toActionTypeEntity(ActionTypeAvro actionTypeAvro) {
        return ActionType.valueOf(actionTypeAvro.name());
    }

    private void checkHubEvent(HubEventAvro hubEvent) throws RuntimeException {
        ScenarioAddedEventAvro event = (ScenarioAddedEventAvro) hubEvent.getPayload();
        List<String> idsConditions = event.getConditions().stream().map(ScenarioConditionAvro::getSensorId).toList();
        List<String> idsActions = event.getActions().stream().map(DeviceActionAvro::getSensorId).toList();

        if (sensorRepository.existsByIdInAndHubId(idsConditions, hubEvent.getHubId())) {
            throw new RuntimeException("Не найдены conditions");
        }
        if (sensorRepository.existsByIdInAndHubId(idsActions, hubEvent.getHubId())) {
            throw new RuntimeException("Не найдены actions");
        }
    }
}

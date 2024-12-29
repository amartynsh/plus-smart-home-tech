package ru.yandex.practicum.handler.hub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.ActionRepository;
import ru.yandex.practicum.repository.ConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.repository.SensorRepository;

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

    @Transactional
    @Override
    public void handle(HubEventAvro hubEventAvro) {
        log.info("Начали обработку hubEventAvro {}", hubEventAvro);
        ScenarioAddedEventAvro scenarioAddedEventAvro = (ScenarioAddedEventAvro) hubEventAvro.getPayload();
        Optional<Scenario> scenarioOld = scenarioRepository.findByHubIdAndName(hubEventAvro.getHubId(),
                scenarioAddedEventAvro.getName());

        Scenario scenario = new Scenario();
        Scenario scenarioWithId = new Scenario();

        if (checkHubEvent(hubEventAvro)) {
            throw new NotFoundException("Sensor из запроса отсутствует");
        }

        if (!scenarioOld.isPresent()) {
            log.info("Сценария не существует, поэтому создаем новый");
            scenario = avroToScenarioEntity(hubEventAvro, scenarioAddedEventAvro);
            log.info("Создали сценарий {}", scenario);
            Scenario finalScenario = scenario;
            List<Condition> conditions =scenarioAddedEventAvro.getConditions().stream()
                    .map(conditionAvro -> avroToConditionEntity(finalScenario, conditionAvro))
                    .toList();
            List<Action> actions = scenarioAddedEventAvro.getActions().stream()
                    .map(actionAvro -> toActionEntity(finalScenario, actionAvro))
                    .toList();
            scenarioWithId = scenarioRepository.save(scenario);
            scenario.setConditions(conditions);
            scenario.setActions(actions);


        } else {
            log.info("Начали обработку ELSE");
            scenario = scenarioOld.get();

            Scenario finalScenario = scenario;
            List<Condition> conditions = scenarioAddedEventAvro.getConditions().stream()
                    .map(conditionAvro -> avroToConditionEntity(finalScenario, conditionAvro))
                    .toList();
            List<Action> actions = scenarioAddedEventAvro.getActions().stream()
                    .map(actionAvro -> toActionEntity(finalScenario, actionAvro))
                    .toList();
            scenarioWithId = scenarioRepository.save(scenario);
            scenario.setConditions(conditions);
            scenario.setActions(actions);
        }


        Long id = scenarioWithId.getId();
        //Удаляем старые условия и действия

        for (Action action : scenario.getActions()) {
            action.setScenario(scenario);
        }

        for (Condition condition : scenario.getConditions()) {
            condition.setScenario(scenario);
        }

        if (scenario.getActions() != null) {
            actionRepository.deleteAllByScenarioId(scenario.getId());
        }
        if (scenario.getConditions() != null) {
            conditionRepository.deleteAllByScenarioId(scenario.getId());
        }
        conditionRepository.saveAll(scenario.getConditions());
        actionRepository.saveAll(scenario.getActions());
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

        return Condition.builder()
                .sensor(new Sensor(scenarioConditionAvro.getSensorId(), scenario.getHubId()))
                .type(avroToConditioTypeEntity(scenarioConditionAvro.getType()))
                .operation(avroToConditionOperationEntity(scenarioConditionAvro.getOperation()))
                .value(getConditionValue(scenarioConditionAvro))
                .scenario(scenario)
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
        log.info("Проверка на петлю {}", deviceActionAvro);
        return Action.builder()
                .sensor(new Sensor(deviceActionAvro.getSensorId(), scenario.getHubId()))
                .type(toActionTypeEntity(deviceActionAvro.getType()))
                .value(deviceActionAvro.getValue())
                .build();
    }

    public ActionType toActionTypeEntity(ActionTypeAvro actionTypeAvro) {
        return ActionType.valueOf(actionTypeAvro.name());
    }

    private boolean checkHubEvent(HubEventAvro hubEvent) throws RuntimeException {
        ScenarioAddedEventAvro event = (ScenarioAddedEventAvro) hubEvent.getPayload();
        List<String> idsConditions = event.getConditions().stream().map(ScenarioConditionAvro::getSensorId).toList();
        log.info("Значение списка {}", idsConditions);
        List<String> idsActions = event.getActions().stream().map(DeviceActionAvro::getSensorId).toList();
        log.info("Значение списка {}", idsActions);
        if (!sensorRepository.existsByIdInAndHubId(idsConditions, hubEvent.getHubId()) && !idsConditions.isEmpty()) {
            return true;
        }
        if (!sensorRepository.existsByIdInAndHubId(idsActions, hubEvent.getHubId()) && !idsActions.isEmpty()) {
            return true;
        }
        return false;
    }
}
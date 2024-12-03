package ru.practicum.model.hub;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.hub.constants.ConditionOperation;
import ru.practicum.model.hub.constants.ConditionType;


@Getter
@Setter
public class ScenarioCondition {

    private String sensorId;
    private ConditionType type;
    private ConditionOperation operation;
    private Boolean value;
}

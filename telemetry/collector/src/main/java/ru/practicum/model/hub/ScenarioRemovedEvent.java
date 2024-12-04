package ru.practicum.model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.hub.constants.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioRemovedEvent extends HubEvent {
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
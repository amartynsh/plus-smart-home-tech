package model.hub;

import lombok.Getter;
import lombok.Setter;
import model.hub.constants.ActionType;

@Getter
@Setter
public class DeviceAction {
    private String sensorId;
    private ActionType type;
    private Integer value;
}

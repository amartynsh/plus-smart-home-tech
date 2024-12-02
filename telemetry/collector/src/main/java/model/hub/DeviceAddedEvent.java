package model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.hub.constants.DeviceType;
import model.hub.constants.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceAddedEvent extends HubEvent {

    private String id;
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}

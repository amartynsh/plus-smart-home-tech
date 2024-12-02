package service;

import model.hub.HubEvent;
import model.sensor.SensorEvent;

public interface CollectorService {
    void sendSensorEvent(SensorEvent event);

    void sendHubEvent(HubEvent event);
}

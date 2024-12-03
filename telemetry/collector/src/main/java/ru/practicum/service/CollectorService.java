package ru.practicum.service;

import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.sensor.SensorEvent;

public interface CollectorService {
    void sendSensorEvent(SensorEvent event);

    void sendHubEvent(HubEvent event);
}
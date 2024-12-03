package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.sensor.SensorEvent;
import ru.practicum.service.CollectorService;

@Slf4j
@AllArgsConstructor
@RequestMapping("/events")
@RestController
public class EventController {

    private final CollectorService collectorService;

    @PostMapping(value = "/sensors")
    public void collectSensors(@RequestBody SensorEvent event) {
        log.info("Обращение на /sensors с событием {}", event.toString());
        collectorService.sendSensorEvent(event);

    }

    @PostMapping(value = "/hubs")
    public void collectHubs(@RequestBody HubEvent event) {
        log.info("Обращение на /hubs с событием {}", event.toString());
        collectorService.sendHubEvent(event);
    }

}

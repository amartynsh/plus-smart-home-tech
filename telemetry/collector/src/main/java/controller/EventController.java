package controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.hub.HubEvent;
import model.sensor.SensorEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.CollectorService;

@Slf4j
@AllArgsConstructor
@RequestMapping("/events")
@RestController
@Valid
public class EventController {

    private final CollectorService collectorService;

    @PostMapping(value = "/sensors")
    public void collectSensors(@Validated @RequestBody SensorEvent event) {
        log.info("Обращение на /sensors с событием {}", event.toString());
        collectorService.sendSensorEvent(event);

    }

    @PostMapping(value = "/hubs")
    public void collectHubs(@Validated @RequestBody HubEvent event) {
        log.info("Обращение на /hubs с событием {}", event.toString());
        collectorService.sendHubEvent(event);
    }

}

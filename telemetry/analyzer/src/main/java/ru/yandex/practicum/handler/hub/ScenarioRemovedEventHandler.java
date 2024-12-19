package ru.yandex.practicum.handler.hub;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.repository.ScenarioRepository;

@AllArgsConstructor
@Component
public class ScenarioRemovedEventHandler implements HubEventHandler {

    private final ScenarioRepository scenarioRepository;

    @Override
    public String getHubEventClassName() {
        return ScenarioRemovedEventAvro.class.getName();
    }

    @Override
    @Transactional
    public void handle(HubEventAvro hubEventAvro) {
        String scenarioRemovedName = ((ScenarioRemovedEventAvro) hubEventAvro.getPayload()).getName();
        scenarioRepository.deleteByHubIdAndName(hubEventAvro.getHubId(), scenarioRemovedName);
    }
}
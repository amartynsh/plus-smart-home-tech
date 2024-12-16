package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.mapper.SnapshotMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service

public class AggregatorService {
    Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();
    SensorStateAvro oldState = new SensorStateAvro();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro eventAvro) {
        SensorsSnapshotAvro snapshotAvro = new SensorsSnapshotAvro();

        if (!snapshots.containsKey(eventAvro.getHubId())) {
            snapshots.put(eventAvro.getHubId(), SnapshotMapper.EventToSnapshot(eventAvro));
            snapshotAvro = snapshots.get(eventAvro.getHubId());
        } else {
            snapshotAvro = snapshots.get(eventAvro.getHubId());
        }
        if (snapshotAvro.getSensorsState().containsKey(eventAvro.getId())) {
            oldState = snapshotAvro.getSensorsState().get(eventAvro.getId());
            if (oldState.getTimestamp().isBefore(eventAvro.getTimestamp()) &&
                    oldState.getData().equals(eventAvro.getPayload())) {
                return Optional.empty();
            }
        }
        SensorStateAvro stateAvro = SnapshotMapper.EventToState(eventAvro);
        snapshots.get(eventAvro.getHubId()).getSensorsState().put(eventAvro.getId(), stateAvro);
        snapshots.get(eventAvro.getHubId()).setTimestamp(eventAvro.getTimestamp());
        return Optional.of(snapshots.get(eventAvro.getHubId()));
    }


}





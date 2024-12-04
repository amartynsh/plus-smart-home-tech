package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.kafka.EventClient;
import ru.practicum.mapper.HubEventMapper;
import ru.practicum.mapper.SensorEventMapper;
import ru.practicum.model.hub.HubEvent;
import ru.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Slf4j
@Service
public class CollectorServiceImpl implements CollectorService {
    private final EventClient eventClient;

    @Value("${spring.kafka.topics.sensor-topic}")
    private String sensorTopic;

    @Value("${spring.kafka.topics.hub-topic}")
    private String hubTopic;

    public CollectorServiceImpl(EventClient eventClient) {
        this.eventClient = eventClient;
    }

    @Override
    public void sendSensorEvent(SensorEvent event) {
        log.info("Началась обработка SensorEvent {}", event.toString());
        SensorEventAvro sensorEventAvro = SensorEventMapper.map(event);
        eventClient.getProducer().send(new ProducerRecord<>(sensorTopic,
                sensorEventAvro));
        log.info("Топик: {}", sensorTopic);
        log.info("Обработка SensorEvent завершена, в KAFKA ушло:  {}", sensorEventAvro);
    }

    @Override
    public void sendHubEvent(HubEvent event) {
        log.info("Началась обработка HubEvent {}", event.toString());
        HubEventAvro hubEventAvro = HubEventMapper.map(event);
        eventClient.getProducer().send(new ProducerRecord<>(hubTopic,
                hubEventAvro));
        log.info("Топик: {}", hubTopic);
        log.info("Обработка HubEvent завершена, в KAFKA ушло: {}", hubEventAvro);
    }
}
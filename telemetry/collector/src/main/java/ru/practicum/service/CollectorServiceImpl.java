package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.kafka.EventClient;
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
    public void sendSensorEvent(SensorEventAvro event) {
        log.info("Началась обработка SensorEvent {}", event.toString());
        eventClient.getProducer().send(new ProducerRecord<>(sensorTopic,
                event));
        log.info("Топик: {}", sensorTopic);
        log.info("Обработка SensorEvent завершена, в KAFKA ушло:  {}", event);
    }

    @Override
    public void sendHubEvent(HubEventAvro event) {
        log.info("Началась обработка HubEvent {}", event.toString());
        eventClient.getProducer().send(new ProducerRecord<>(hubTopic,
                event));
        log.info("Топик: {}", hubTopic);
        log.info("Обработка HubEvent завершена, в KAFKA ушло: {}", event);
    }
}
package service;

import kafka.EventClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.HubEventMapper;
import mapper.SensorEventMapper;
import model.hub.HubEvent;
import model.sensor.SensorEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class CollectorServiceImpl implements CollectorService {
    EventClient eventClient;

    @Override
    public void sendSensorEvent(SensorEvent event) {
        log.info("Началась обработка SensorEvent {}", event.toString());
        eventClient.getProducer().send(new ProducerRecord<>(eventClient.getSensorTopic(),
                SensorEventMapper.map(event)));
        log.info("Обработка SensorEvent завершена, в KAFKA ушло:  {}", SensorEventMapper.map(event));
    }

    @Override
    public void sendHubEvent(HubEvent event) {
        log.info("Началась обработка HubEvent {}", event.toString());
        eventClient.getProducer().send(new ProducerRecord<>(eventClient.getHubTopic(),
                HubEventMapper.map(event)));
        log.info("Обработка HubEvent завершена, в KAFKA ушло: {}", HubEventMapper.map(event));
    }
}
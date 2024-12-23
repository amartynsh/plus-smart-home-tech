package ru.yandex.practicum.kafka.consumer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Getter
@Slf4j
@Component

public class ConsumerHubProperties {
    @Autowired
    @Qualifier("hub")
    KafkaConsumerProperties config;

    public Properties getConfig() {
        return config.getConfig();
    }
}
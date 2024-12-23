package ru.yandex.practicum.kafka.consumer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigClass {

    @Bean(name = "snapshot")
    @Qualifier("snapshot")
    @ConfigurationProperties(prefix = "spring.kafka.consumer-snapshot")
    public KafkaConsumerProperties kafkaSnaphotProperties() {
        return new KafkaConsumerProperties();
    }

    @Bean(name = "hub")
    @Qualifier("hub")
    @ConfigurationProperties(prefix = "spring.kafka.consumer-hub")
    public KafkaConsumerProperties kafkaHubProperties() {
        return new KafkaConsumerProperties();
    }
}

package ru.yandex.practicum.kafka.consumer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class ConfigClass {

    @Bean(name = "snapshot")
    @Qualifier("snapshot")
    @ConfigurationProperties(prefix = "spring.kafka.consumer-snapshot")
    private KafkaConsumerProperties kafkaSnaphotProperties() {
        return new KafkaConsumerProperties();
    }

    @Bean(name = "hub")
    @Qualifier("hub")
    @ConfigurationProperties(prefix = "spring.kafka.consumer-hub")
    private KafkaConsumerProperties kafkaHubProperties() {
        return new KafkaConsumerProperties();
    }

    public Properties getSnapshotProperites() {
        return kafkaSnaphotProperties().getConfig();
    }

    public Properties getHubProperties() {
        return kafkaHubProperties().getConfig();
    }
}

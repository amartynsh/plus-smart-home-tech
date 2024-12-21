package ru.yandex.practicum.kafka.consumer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


@Getter


@Slf4j
@Configuration
public class ConsumerSnapshotProperties {
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;
    @Value("${spring.kafka.consumer.value-snapshot-deserializer}")
    private String valueDeserializer;
    @Value("${spring.kafka.consumer.client-id-analyzer}")
    private String clientId;
    @Value("${spring.kafka.consumer.group-id-analyzer}")
    private String groupId;
    @Value("${spring.kafka.consumer.max-poll-records}")
    private String maxPollRecords;
    @Value("${spring.kafka.consumer.max-poll-records}")
    private String fetchMaxBytes;
    @Value("${spring.kafka.consumer.max-partition-fetch-bytes}")
    private String maxPartitionFetchBytes;


    public Properties getConfig() {
        Properties properties = new Properties();
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.FETCH_MAX_BYTES_CONFIG, fetchMaxBytes);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        return properties;
    }
}
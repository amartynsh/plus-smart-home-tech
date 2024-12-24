package ru.yandex.practicum.kafka.consumer;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
@Getter
@Setter
public class KafkaConsumerProperties {
    private String bootstrapServers;
    private String keyDeserializer;
    private String valueDeserializer;
    private String clientId;
    private String groupId;
    private String maxPollRecords;
    private int fetchMaxBytes;
    private int maxPartitionFetchBytes;

    public Properties getConfig() {

        Properties properties = new Properties();
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.CLIENT_ID_CONFIG, clientId );
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, groupId);
        log.info("Properties properties = {}", properties);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        log.info("Properties properties = {}", properties);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,valueDeserializer);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.FETCH_MAX_BYTES_CONFIG, fetchMaxBytes);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        return properties;
    }
}
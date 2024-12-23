package ru.yandex.practicum.kafka.consumer;

import lombok.Getter;
import lombok.Setter;

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
}
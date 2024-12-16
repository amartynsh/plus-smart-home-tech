package ru.yandex.practicum.kafka.producer;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;

public interface EventProducer {

    Producer<String, SpecificRecordBase> getProducer();

    void stop();
}
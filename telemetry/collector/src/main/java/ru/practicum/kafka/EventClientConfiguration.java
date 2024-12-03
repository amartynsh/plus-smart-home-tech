package ru.practicum.kafka;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class EventClientConfiguration {


    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;
    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Bean
    EventClient getClient() {
        return new EventClient() {

            private Producer<String, SpecificRecordBase> producer;

            @Override
            public Producer<String, SpecificRecordBase> getProducer() {
                if (producer == null) {
                    initProducer();
                }
                return producer;
            }

            private void initProducer() {
                Properties config = new Properties();
                config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                        bootstrapServers);
                config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                        keySerializer);
                config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                        valueSerializer);

                producer = new KafkaProducer<>(config);
            }

            @PreDestroy
            @Override
            public void stop() {
                if (producer != null) {
                    producer.close();
                }
            }
        };
    }
}
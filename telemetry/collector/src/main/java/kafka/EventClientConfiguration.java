package kafka;

import jakarta.annotation.PreDestroy;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Properties;

@Configuration
public class EventClientConfiguration implements EnvironmentAware {
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

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
                        environment.getProperty("spring.kafka.producer.bootstrap-servers"));
                config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                        environment.getProperty("spring.kafka.producer.key-serializer"));
                config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                        environment.getProperty("spring.kafka.producer.value-serializer"));

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
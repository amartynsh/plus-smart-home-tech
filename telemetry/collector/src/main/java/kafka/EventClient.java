package kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;

public interface EventClient {

    Producer<String, SpecificRecordBase> getProducer();

    String getSensorTopic();

    String getHubTopic();

    void stop();
}


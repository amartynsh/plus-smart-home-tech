package kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;

public interface EventClient {

    Producer<String, SpecificRecordBase> getProducer();

    void stop();
}


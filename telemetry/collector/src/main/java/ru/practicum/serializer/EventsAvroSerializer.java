package ru.practicum.serializer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class EventsAvroSerializer implements Serializer<SpecificRecordBase> {
    private final EncoderFactory encoderFactory = EncoderFactory.get();
    private BinaryEncoder encoder;

    public byte[] serialize(String topic, SpecificRecordBase data) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            log.info("serialize start");
            byte[] result = null;
            encoder = encoderFactory.binaryEncoder(out, encoder);
            if (data != null) {
                log.info("serialize start1");
                DatumWriter<SpecificRecordBase> writer = new SpecificDatumWriter<>(data.getSchema());
                log.info("Схема топика: " + data.getSchema());
                log.info("serialize start2");
                writer.write(data, encoder);
                log.info("serialize start3");
                encoder.flush();
                log.info("serialize start4");
                result = out.toByteArray();
                log.info("serialize end");
            }
            return result;
        } catch (IOException ex) {
            throw new SerializationException("Ошибка сериализации данных для топика [" + topic + "]", ex);
        }
    }
}
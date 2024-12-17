package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.KafkaTopics;
import ru.yandex.practicum.kafka.consumer.ConsumerProperties;
import ru.yandex.practicum.kafka.producer.EventProducer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    @Value(value = "${spring.kafka.consumer.consume-attempts-timeout-ms}")
    private Duration consumeAttemptTimeout;
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();// снимок состояния
    private final ConsumerProperties consumerConfig;
    private final AggregatorService aggregatorService;
    private final EventProducer eventProducer;
    private final KafkaTopics kafkaTopics;


    private static void manageOffsets(ConsumerRecord<String, SensorEventAvro> record, int count, KafkaConsumer<String, SensorEventAvro> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }

    public void start() {
        Properties config = consumerConfig.getConfig();
        KafkaConsumer<String, SensorEventAvro> consumer = new KafkaConsumer<>(config);
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            consumer.subscribe(List.of(kafkaTopics.getSensorTopic()));
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(consumeAttemptTimeout);

                int count = 0;
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    // обрабатываем очередную запись
                    handleRecord(record);
                    // фиксируем оффсеты обработанных записей, если нужно
                    manageOffsets(record, count, consumer);
                    count++;
                }
                // фиксируем максимальный оффсет обработанных записей
                consumer.commitAsync();

            }
        } catch (WakeupException | InterruptedException ignores) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();

            }
        }
    }

    private void handleRecord(ConsumerRecord<String, SensorEventAvro> record) throws InterruptedException {

        log.info("топик = {}, партиция = {}, смещение = {}, значение: {}\n",
                record.topic(), record.partition(), record.offset(), record.value());
        Optional<SensorsSnapshotAvro> result = aggregatorService.updateState(record.value());
        if (result.isPresent()) {
            log.info("Обновляем состояние агрегатора: {}", result.get());
            eventProducer.getProducer().send(new ProducerRecord<>(kafkaTopics.getSnapshotTopic(), result.get()));
        }
    }
}
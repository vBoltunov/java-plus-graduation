package ru.practicum.service.similarity;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.config.KafkaConfig;
import ru.practicum.config.KafkaTopic;
import ru.practicum.model.similarity.Similarity;
import ru.practicum.model.similarity.SimilarityCompositeKey;
import ru.practicum.repository.SimilarityRepository;
import ru.practicum.EventSimilarityAvro;

import java.time.Duration;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class SimilarityProcessor implements Runnable {
    private final SimilarityRepository similarityRepository;
    private final EnumMap<KafkaTopic, String> topics;
    private final KafkaConsumer<Long, EventSimilarityAvro> consumer;

    public SimilarityProcessor(KafkaConfig kafkaConfig, SimilarityRepository repository) {
        this.similarityRepository = repository;
        topics = kafkaConfig.getTopics();
        consumer = new KafkaConsumer<>(kafkaConfig.getSimilarityConsumerProps());
    }

    @Override
    public void run() {
        consumer.subscribe(List.of(topics.get(KafkaTopic.EVENTS_SIMILARITY)));
        AtomicBoolean running = new AtomicBoolean(true);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            running.set(false);
            consumer.wakeup();
        }));

        try {
            while (running.get()) {
                ConsumerRecords<Long, EventSimilarityAvro> records = consumer.poll(Duration.ofMillis(5000));
                if (records.isEmpty()) continue;

                for (ConsumerRecord<Long, EventSimilarityAvro> consumerRecord : records) {
                    Similarity similarity = new Similarity();
                    similarity.setKey(new SimilarityCompositeKey(
                            consumerRecord.value().getEventA(),
                            consumerRecord.value().getEventB()
                    ));
                    similarity.setScore(consumerRecord.value().getScore());
                    similarityRepository.save(similarity);
                }

                consumer.commitAsync((offsets, exception) -> {
                    if (exception != null) {
                        log.warn("Ошибка при фиксации смещений: {}", offsets, exception);
                    }
                });
            }
        } catch (WakeupException ignored) {
            // Корректная остановка потребителя
        } catch (Exception error) {
            log.error("Ошибка при обработке схожести событий", error);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    public void stop() {
        consumer.wakeup();
    }
}

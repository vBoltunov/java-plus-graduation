package ru.practicum.service.action;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.UserActionAvro;
import ru.practicum.config.KafkaConfig;
import ru.practicum.config.KafkaTopic;
import ru.practicum.model.action.ActionType;

import java.time.Duration;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class ActionProcessor implements Runnable {
    private final ActionService actionService;
    private final EnumMap<KafkaTopic, String> topics;
    private final KafkaConsumer<Long, UserActionAvro> consumer;

    public ActionProcessor(KafkaConfig kafkaConfig,
                           ActionService actionService) {
        this.actionService = actionService;
        topics = kafkaConfig.getTopics();
        consumer = new KafkaConsumer<>(kafkaConfig.getActionConsumerProps());
    }

    @Override
    public void run() {
        consumer.subscribe(List.of(topics.get(KafkaTopic.USER_ACTIONS)));
        AtomicBoolean running = new AtomicBoolean(true);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            running.set(false);
            consumer.wakeup();
        }));

        try {
            while (running.get()) {
                ConsumerRecords<Long, UserActionAvro> records = consumer.poll(Duration.ofMillis(5000));
                if (records.isEmpty()) continue;

                for (ConsumerRecord<Long, UserActionAvro> consumerRecord : records) {
                    actionService.addUserAction(
                            consumerRecord.value().getUserId(),
                            consumerRecord.value().getEventId(),
                            ActionType.valueOf(consumerRecord.value().getActionType().name()),
                            consumerRecord.value().getTimestamp()
                    );
                }
                consumer.commitAsync((offsets, exception) -> {
                    if (exception != null) {
                        log.warn("Ошибка при фиксации смещений: {}", offsets, exception);
                    }
                });
            }
        } catch (WakeupException ignored) {
            // Остановка потребителя
        } catch (Exception error) {
            log.error("Ошибка при обработке действий пользователя", error);
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

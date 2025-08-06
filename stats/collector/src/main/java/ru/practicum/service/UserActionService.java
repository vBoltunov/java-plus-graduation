package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ActionTypeAvro;
import ru.practicum.UserActionAvro;
import ru.practicum.grpc.stats.action.UserActionProto;
import ru.practicum.kafka.KafkaTopic;
import ru.practicum.kafka.UserActionKafkaProducer;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActionService {
    private final UserActionKafkaProducer kafkaProducer;

    public void collectUserAction(UserActionProto request) {
        ActionTypeAvro actionType;

        switch (request.getActionType()) {
            case ACTION_VIEW -> actionType = ActionTypeAvro.VIEW;
            case ACTION_LIKE -> actionType = ActionTypeAvro.LIKE;
            case ACTION_REGISTER -> actionType = ActionTypeAvro.REGISTER;
            default -> throw new IllegalArgumentException(String.format("Такого типа нет: %s", request.getActionType()));
        }

        UserActionAvro avro = UserActionAvro.newBuilder()
                .setUserId(request.getUserId())
                .setActionType(actionType)
                .setEventId(request.getEventId())
                .setTimestamp(Instant.ofEpochSecond(request.getTimestamp().getSeconds(), request.getTimestamp().getNanos()))
                .build();

        kafkaProducer.send(avro, KafkaTopic.USER_ACTIONS);
    }
}

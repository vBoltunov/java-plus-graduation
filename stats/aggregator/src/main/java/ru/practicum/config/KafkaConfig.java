package ru.practicum.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Properties;

@Getter
@Setter
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties("aggregator.kafka")
public class KafkaConfig {
    EnumMap<KafkaTopic, String> topics = new EnumMap<>(KafkaTopic.class);
    Properties producerProps;
    Properties consumerProps;
}

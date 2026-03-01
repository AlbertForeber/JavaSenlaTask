package com.senla;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

    private final String bootstrapServers;

    public KafkaProducerConfig(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    @Bean
    public ProducerFactory<String, MoneyTransfer> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Для exactly-once
            // Для идемпотентности
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

            // Для транзакционности
        config.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "producer-service-1");

        DefaultKafkaProducerFactory<String, MoneyTransfer> producerFactory = new DefaultKafkaProducerFactory<>(config);
//        producerFactory.setTransactionIdPrefix("producer-service-1");

        return producerFactory;
    }

    @Bean
    public KafkaTemplate<String, MoneyTransfer> kafkaTemplate() {
        KafkaTemplate<String, MoneyTransfer> template = new KafkaTemplate<>(producerFactory());

        template.setDefaultTopic("test-topic");
        return template;
    }

    @Bean
    public KafkaTransactionManager<String, MoneyTransfer> kafkaTransactionManager() {
        return new KafkaTransactionManager<>(producerFactory());
    }
}

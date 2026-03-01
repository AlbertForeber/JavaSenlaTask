package com.senla.config;

import com.senla.entity.MoneyTransferProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    private final String bootstrapServers;

    public KafkaConsumerConfig(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    @Bean
    public ConsumerFactory<String, MoneyTransferProducer> consumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group-1");

        // Требования к одному poll-у, чтобы набиралось больше сообщений
        config.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1000);
        config.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 10_000);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.senla");

        // Т.к. в producer и consumer нужные модели имеют разное название классов
        // и находится в разных пакетах - возникает конфликт
        // т.к. передаваемый JSON хранит путь к предыдущей модели (com.senla.MoneyTransfer)
        config.put(JsonDeserializer.TYPE_MAPPINGS, "com.senla.MoneyTransfer:com.senla.entity.MoneyTransferProducer");

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MoneyTransferProducer> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MoneyTransferProducer> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3); // Количество консьюмеров
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setPollTimeout(5_000); // Если консьюмер зависнет на это время - считается мертвым

        return factory;
    }
}

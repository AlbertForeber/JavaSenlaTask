package com.senla;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaListeners {
    private static final Logger logger = LoggerFactory.getLogger(KafkaListeners.class);

    @KafkaListener(
            topics = "test-topic",
            groupId = "group-1",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenBatch(List<String> messages) throws InterruptedException {
        logger.info("Got messages: {}. Thread ID: {}", messages, Thread.currentThread().threadId());
//        Thread.sleep(3000); // Пытался так растянуть время между poll-ами, чтобы получало пачку
    }
}

package com.senla;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MessageProduceService {

    private static final Logger logger = LoggerFactory.getLogger(MessageProduceService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    public MessageProduceService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional// Вместе с Scheduled создает проблемы
    public void sendSimple() throws ExecutionException, InterruptedException, TimeoutException {
        logger.debug("Message sent at {}", LocalDateTime.now());
        try {
            for (int i = 0; i < 5; i ++) {
                CompletableFuture<SendResult<String, String>> a = kafkaTemplate.send("test-topic", "Hello");
                a.get(10, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.trace(e.getMessage());
            throw e;
        }
    }
}

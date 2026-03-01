package com.senla;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MessageProduceService {

    private static final Logger logger = LoggerFactory.getLogger(MessageProduceService.class);
    private final KafkaTemplate<String, MoneyTransfer> kafkaTemplate;

    public MessageProduceService(KafkaTemplate<String, MoneyTransfer> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional // Вместе с Scheduled иногда может создавать проблемы
    public void sendSimple() throws ExecutionException, InterruptedException, TimeoutException {
        logger.debug("Message sent at {}", LocalDateTime.now());
        try {
            Random random = new Random();

            for (int i = 0; i < 5; i ++) {
                CompletableFuture<SendResult<String, MoneyTransfer>> a = kafkaTemplate
                        .send("test-topic", new MoneyTransfer(
                                null,
                                random.nextInt(1, 1001),
                                random.nextInt(1, 1001),
                                random.nextInt(10_000, 100_000)
                        ));
                a.get(10, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.trace(e.getMessage());
            throw e;
        }
    }
}

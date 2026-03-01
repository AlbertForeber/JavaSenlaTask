package com.senla.config;

import com.senla.db.DbActions;
import com.senla.db.SimpleGenericDao;
import com.senla.entity.Account;
import com.senla.entity.MoneyTransfer;
import com.senla.entity.MoneyTransferProducer;
import com.senla.entity.Status;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class KafkaListeners {
    private static final Logger logger = LoggerFactory.getLogger(KafkaListeners.class);
    private final DbActions dbActions;

    public KafkaListeners(DbActions dbActions) {
        this.dbActions = dbActions;
    }

    @KafkaListener(
            topics = "test-topic",
            groupId = "group-1",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenBatch(
            List<MoneyTransferProducer> messages,
            Acknowledgment acknowledgment
    ) {
//        logger.info("Got messages: {}. Thread ID: {}", messages, Thread.currentThread().threadId());
//        Thread.sleep(3000); // Пытался так растянуть время между poll-ами, чтобы получало пачку
        for (MoneyTransferProducer transferInfo : messages) {
            dbActions.performTransaction(transferInfo);
        }
        acknowledgment.acknowledge();
    }
}

package com.senla.config;

import com.senla.db.SimpleGenericDao;
import com.senla.entity.Account;
import com.senla.entity.MoneyTransfer;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class KafkaListeners {
    private static final Logger logger = LoggerFactory.getLogger(KafkaListeners.class);
    private final Random random = new Random();

    private final SimpleGenericDao<Account>         accountDao;
    private final SimpleGenericDao<MoneyTransfer>   moneyTransferDao;

    public KafkaListeners(SimpleGenericDao<Account> accountDao, SimpleGenericDao<MoneyTransfer> moneyTransferDao) {
        this.accountDao = accountDao;
        this.moneyTransferDao = moneyTransferDao;
    }

    @KafkaListener(
            topics = "test-topic",
            groupId = "group-1",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenBatch(List<String> messages) throws InterruptedException {
        logger.info("Got messages: {}. Thread ID: {}", messages, Thread.currentThread().threadId());
//        Thread.sleep(3000); // Пытался так растянуть время между poll-ами, чтобы получало пачку
    }

    // Аналог PostConstruct на более позднем моменте
    // Контекст инициализирован, прокси создан, все работает
    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void fillDatabaseIfEmpty() {
        Account firstAccount = accountDao.findById(1);

        if (firstAccount == null) {
            for (int i = 0; i < 1000; i ++) {

                Account newAccount = new Account(
                        null,
                        random.nextInt(10000, 100000)
                );

                accountDao.save(newAccount);
            }
        }
    }
}

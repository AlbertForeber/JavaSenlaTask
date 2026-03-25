package com.senla.db;

import com.senla.entity.Account;
import com.senla.entity.MoneyTransfer;
import com.senla.entity.MoneyTransferProducer;
import com.senla.entity.Status;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DbActions {

    private static final Logger logger = LoggerFactory.getLogger(DbActions.class);
    private final SimpleGenericDao<Account>         accountDao;
    private final SimpleGenericDao<MoneyTransfer>   moneyTransferDao;

    public DbActions(SimpleGenericDao<Account> accountDao, SimpleGenericDao<MoneyTransfer> moneyTransferDao) {
        this.accountDao = accountDao;
        this.moneyTransferDao = moneyTransferDao;
    }

    @Transactional
    // Batch добавить
    public void performTransaction(MoneyTransferProducer transferInfo) {
        Account sender = accountDao.findById(transferInfo.getSenderId());
        Account receiver = accountDao.findById(transferInfo.getReceiverId());

        Status status = Status.SUCCESS;
        StringBuilder builder = new StringBuilder();

        if (sender == null)
            builder.append(transferInfo.getSenderId());

        if (receiver == null)
            builder.append(", ").append(transferInfo.getReceiverId());

        if (!builder.isEmpty()) {
            logger.error("Счет с номерами: {} не найдены", builder);
            status = Status.ERROR;
        }

        else if (sender.getBalance() < transferInfo.getTransferAmount()) {
            logger.error("На счету отправителя недостаточно средств");
            status = Status.ERROR;
        }

        if (status != Status.ERROR) {
            sender.setBalance(sender.getBalance() - transferInfo.getTransferAmount());
            receiver.setBalance(receiver.getBalance() + transferInfo.getTransferAmount());

            accountDao.save(sender);
            accountDao.save(receiver);
        }

        moneyTransferDao.save(new MoneyTransfer(null, sender, receiver, transferInfo.getTransferAmount(), status));
    }

    // Аналог PostConstruct на более позднем моменте
    // Контекст инициализирован, прокси создан, все работает
    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void fillDatabaseIfEmpty() {
        Account firstAccount = accountDao.findById(1);

        if (firstAccount == null) {
            Random random = new Random();

            for (int i = 0; i < 1000; i++) {

                Account newAccount = new Account(
                        null,
                        random.nextInt(10000, 100000)
                );

                accountDao.save(newAccount);
            }
        }
    }
}

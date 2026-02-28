package com.senla;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public class MessageProduceScheduler {

    private final MessageProduceService messageProduceService;

    public MessageProduceScheduler(MessageProduceService messageProduceService) {
        this.messageProduceService = messageProduceService;
    }

    @Scheduled(fixedDelay = 200, initialDelay = 30000)
    public void sendSimple() throws ExecutionException, InterruptedException, TimeoutException {
        messageProduceService.sendSimple();
    }
}

package com.senla.entity;

public class MoneyTransferProducer {

    private Integer id;
    private int senderId;
    private int receiverId;
    private int transferAmount;

    public MoneyTransferProducer() {}

    public MoneyTransferProducer(Integer id, int senderId, int receiverId, int transferAmount) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.transferAmount = transferAmount;
    }

    public Integer getId() {
        return id;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public int getTransferAmount() {
        return transferAmount;
    }
}

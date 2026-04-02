package com.senla;

public class MoneyTransfer {

    private final Integer id;
    private final int senderId;
    private final int receiverId;
    private final int transferAmount;

    public MoneyTransfer(Integer id, int senderId, int receiverId, int transferAmount) {
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

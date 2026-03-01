package com.senla.entity;

import jakarta.persistence.*;

@Entity(name = "transfers")
public class MoneyTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transfer_seq")
    @SequenceGenerator(name = "transfer_seq", sequenceName = "transfers_id_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account", referencedColumnName = "id")
    private Account sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account", referencedColumnName = "id")
    private Account receiver;

    @Column(name = "transfer_amount")
    private int transferAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public MoneyTransfer() {}

    public MoneyTransfer(Integer id, Account sender, Account receiver, int transferAmount, Status status) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.transferAmount = transferAmount;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public Account getSender() {
        return sender;
    }

    public Account getReceiver() {
        return receiver;
    }

    public int getTransferAmount() {
        return transferAmount;
    }
}

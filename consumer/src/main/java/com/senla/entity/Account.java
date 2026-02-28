package com.senla.entity;

import jakarta.persistence.*;

@Entity(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_seq")
    @SequenceGenerator(name = "accounts_seq", sequenceName = "accounts_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "balance")
    private int balance;

    public Account() {}

    public Account(Integer id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }
}

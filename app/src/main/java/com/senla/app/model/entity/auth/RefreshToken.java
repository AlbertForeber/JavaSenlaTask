package com.senla.app.model.entity.auth;

import jakarta.persistence.*;

@Entity(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_seq")
    @SequenceGenerator(name = "refresh_seq", sequenceName = "hello-hello")
    private Long id;


}

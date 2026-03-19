package com.senla.app.model.entity.auth;

import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_seq")
    @SequenceGenerator(name = "refresh_seq", sequenceName = "refresh_tokens_id_seq")
    private Long id;

    @Column(name = "token", unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Temporal(TemporalType.DATE)
    @Column(name = "expiry_date")
    private Instant expiryDate;

    @Column(name = "used")
    private Boolean used;

    @Column(name = "revoked")
    private Boolean revoked;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "replaced_by_token")
    private String replacedByToken;

    public RefreshToken() {
    }

    public RefreshToken(Long id,
                        String token,
                        User user,
                        Instant expiryDate,
                        Boolean used,
                        Boolean revoked,
                        Instant createdAt,
                        String replacedByToken) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
        this.used = used;
        this.revoked = revoked;
        this.createdAt = createdAt;
        this.replacedByToken = replacedByToken;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public Boolean getUsed() {
        return used;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getReplacedByToken() {
        return replacedByToken;
    }
}

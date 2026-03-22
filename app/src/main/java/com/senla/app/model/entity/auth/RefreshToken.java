package com.senla.app.model.entity.auth;

import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_seq")
    @SequenceGenerator(name = "refresh_seq", sequenceName = "refresh_tokens_id_seq", allocationSize = 1)
    private Integer id = null;

    @Column(name = "token", unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date")
    private Instant expiryDate;

    @Column(name = "used")
    private Boolean used = false;

    @Column(name = "revoked")
    private Boolean revoked = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "replaced_by_token")
    private String replacedByToken = "";

    public RefreshToken() {
    }

    public RefreshToken(String token,
                        User user,
                        Instant expiryDate,
                        Instant createdAt) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
    }

    public Integer getId() {
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

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public void setReplacedByToken(String replacedByToken) {
        this.replacedByToken = replacedByToken;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }

    public boolean isValid() {
        return !isExpired() && !revoked && !used;
    }
}

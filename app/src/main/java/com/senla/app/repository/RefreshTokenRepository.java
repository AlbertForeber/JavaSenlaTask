package com.senla.app.repository;

import com.senla.app.model.entity.auth.RefreshToken;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository {

    RefreshToken findByToken(String token);
    RefreshToken findByUsername(String username);

    void deleteByUsername(String username);
    void markAsUsed(String oldToken, String newToken);
}

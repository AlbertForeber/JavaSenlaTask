package com.senla.app.repository;

import com.senla.app.model.entity.auth.RefreshToken;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository {

    RefreshToken findByToken(String token);
    List<RefreshToken> findByUsername(String username);

    void deleteByUsername(String username);
    RefreshToken createOrUpdateToken(RefreshToken token);
    void markAsUsed(String oldToken, String newToken);
    void revokeAllTokensForUser(String username);
}

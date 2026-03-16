package com.senla.app.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration:18000000}")
    private long expirationTime;

    public String generateToken(UserDetails userDetails) {
        return createToken(userDetails);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return (userDetails.getUsername().equals(getUsernameFromToken(token)) && !isExpired(token));
    }

    public String getUsernameFromToken(String token) {
        return getAllClaims(token).getSubject();
    }

    private boolean isExpired(String token) {
        return (new Date()).after(getAllClaims(token).getExpiration());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String createToken(UserDetails userDetails) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .signWith(getSigningKey())
                .issuedAt(now)
                .expiration(expireDate)
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] bytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }
}

package com.tissenza.tissenza_backend.common.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String phone) {
        return Jwts.builder()
            .subject(phone)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getKey())
            .compact();
    }

    public String extractPhone(String token) {
        return Jwts.parser().verifyWith(getKey())
            .build().parseSignedClaims(token)
            .getPayload().getSubject();
    }

    public boolean isValid(String token) {
        try { extractPhone(token); return true; }
        catch (Exception e) { return false; }
    }
}
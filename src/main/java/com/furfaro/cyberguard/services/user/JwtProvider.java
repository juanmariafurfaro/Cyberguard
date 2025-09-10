package com.furfaro.cyberguard.services.user;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey key;     // <-- usar SecretKey
    private final long expirationMs;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {

        // HS256 requiere >= 256 bits (~32 bytes). Tu secreto debe ser largo.
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generateToken(String email) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(email)                          // nuevo API 0.12.x
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(key)                           // infiere HS256 por ser HMAC key
                .compact();
    }

    public String validateAndGetSubject(String token) {
        return Jwts.parser()
                .verifyWith(key)                         // ahora compila: recibe SecretKey
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
package dev.ailuruslabs.ohmyrest.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
class JwtUtil {

    private final Key key;
    private final long expirationInMs;

    JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration-ms}") long expirationInMs) {
        this.expirationInMs = expirationInMs;

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Mono<String> generateToken(Authentication auth) {
        return Mono.fromSupplier(() -> {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expirationInMs);

            return Jwts.builder()
                .subject(auth.getName())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
        });
    }

}

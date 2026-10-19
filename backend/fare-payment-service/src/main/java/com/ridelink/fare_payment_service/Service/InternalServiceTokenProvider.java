package com.ridelink.fare_payment_service.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class InternalServiceTokenProvider {
    private final String secret;
    private final String issuer;
    private final long expirationMs;

    public InternalServiceTokenProvider(@Value("${ridelink.jwt-secret}") String secret,
                                        @Value("${ridelink.jwt-issuer}") String issuer,
                                        @Value("${ridelink.service-token-expiration-ms}") long expirationMs) {
        this.secret = secret;
        this.issuer = issuer;
        this.expirationMs = expirationMs;
    }

    public String token() {
        Instant now = Instant.now();
        return Jwts.builder().subject("fare-payment-service").issuer(issuer)
                .claim("role", "SERVICE").claim("token_type", "SERVICE")
                .issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))).compact();
    }
}

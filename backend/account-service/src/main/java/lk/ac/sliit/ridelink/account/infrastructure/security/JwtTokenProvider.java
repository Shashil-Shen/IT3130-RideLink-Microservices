package lk.ac.sliit.ridelink.account.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lk.ac.sliit.ridelink.account.application.port.out.TokenGenerationPort;
import lk.ac.sliit.ridelink.account.domain.model.Account;
import lk.ac.sliit.ridelink.account.infrastructure.configuration.JwtProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider implements TokenGenerationPort {
    private final JwtProperties properties;
    private final SecretKey key;

    public JwtTokenProvider(JwtProperties properties) {
        this.properties = properties;
        this.key = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generate(Account account) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(account.getId().toString())
                .issuer(properties.issuer())
                .claim("email", account.getEmail())
                .claim("role", account.getRole().name())
                .claim("token_type", "USER")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(properties.expirationMs())))
                .signWith(key)
                .compact();
    }

    @Override
    public long expirationMs() {
        return properties.expirationMs();
    }

    public Claims parse(String token) {
        return Jwts.parser().verifyWith(key).requireIssuer(properties.issuer()).build()
                .parseSignedClaims(token).getPayload();
    }

    public UUID accountId(Claims claims) {
        return UUID.fromString(claims.getSubject());
    }
}

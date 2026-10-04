package lk.ac.sliit.ridelink.account.infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(String secret, long expirationMs, String issuer) 
{
    public JwtProperties {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("JWT secret must be configured");
        }
        if (expirationMs <= 0) {
            throw new IllegalArgumentException("JWT expiration must be positive");
        }
    }
}

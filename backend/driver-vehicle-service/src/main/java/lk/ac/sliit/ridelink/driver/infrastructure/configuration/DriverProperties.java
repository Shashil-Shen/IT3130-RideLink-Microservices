package lk.ac.sliit.ridelink.driver.infrastructure.configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix="ridelink") public record DriverProperties(String accountServiceUrl,String jwtSecret,long serviceTokenExpirationMs,String jwtIssuer) { public DriverProperties { if(accountServiceUrl==null||accountServiceUrl.isBlank())throw new IllegalArgumentException("Account Service URL is required"); if(jwtSecret==null||jwtSecret.length()<32)throw new IllegalArgumentException("JWT secret must contain at least 32 characters"); } }

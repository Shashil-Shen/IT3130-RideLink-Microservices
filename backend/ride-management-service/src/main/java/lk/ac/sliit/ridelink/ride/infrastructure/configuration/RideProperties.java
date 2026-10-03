package lk.ac.sliit.ridelink.ride.infrastructure.configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix="ridelink") public record RideProperties(String accountServiceUrl,String driverServiceUrl,String farePaymentServiceUrl,String jwtSecret,String jwtIssuer,long serviceTokenExpirationMs,int connectTimeoutMs,int readTimeoutMs) {}

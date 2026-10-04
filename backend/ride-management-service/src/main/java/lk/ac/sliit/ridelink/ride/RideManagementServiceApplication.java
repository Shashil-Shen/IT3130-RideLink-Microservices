package lk.ac.sliit.ridelink.ride;

import lk.ac.sliit.ridelink.ride.infrastructure.configuration.RideProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RideProperties.class)
public class RideManagementServiceApplication {
    public static void main(String[] args) { SpringApplication.run(RideManagementServiceApplication.class, args); }
}

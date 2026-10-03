package lk.ac.sliit.ridelink.driver.infrastructure.client;
import java.util.UUID;
public record AccountValidationResponse(UUID accountId,String role,String status,boolean valid) {}

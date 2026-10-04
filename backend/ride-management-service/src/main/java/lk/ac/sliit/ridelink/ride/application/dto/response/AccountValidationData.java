package lk.ac.sliit.ridelink.ride.application.dto.response;
import java.util.UUID;
public record AccountValidationData(UUID accountId,String role,String status,boolean valid) {}

package lk.ac.sliit.ridelink.driver.application.dto.response;
import lk.ac.sliit.ridelink.driver.domain.enums.AvailabilityStatus; import java.math.BigDecimal; import java.time.Instant; import java.util.*;
public record DriverProfileResponse(UUID id,UUID accountId,String licenceNumber,AvailabilityStatus availabilityStatus,String serviceArea,
 BigDecimal currentLatitude,BigDecimal currentLongitude,Instant availableSince,UUID currentRideId,Instant createdAt,Instant updatedAt,List<VehicleResponse> vehicles) {}

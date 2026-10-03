package lk.ac.sliit.ridelink.driver.application.dto.response;
import lk.ac.sliit.ridelink.driver.domain.enums.VehicleType; import java.time.Instant; import java.util.UUID;
public record VehicleResponse(UUID id,UUID driverId,String registrationNumber,String make,String model,String colour,VehicleType vehicleType,int manufacturingYear,boolean active,Instant createdAt,Instant updatedAt) {}

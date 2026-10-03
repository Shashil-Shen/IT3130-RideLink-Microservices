package lk.ac.sliit.ridelink.driver.application.dto.request;
import jakarta.validation.constraints.*; import lk.ac.sliit.ridelink.driver.domain.enums.VehicleType;
public record RegisterVehicleRequest(@NotBlank @Size(max=30) String registrationNumber,@NotBlank @Size(max=60) String make,
 @NotBlank @Size(max=60) String model,@NotBlank @Size(max=40) String colour,@NotNull VehicleType vehicleType,@Min(1980) int manufacturingYear) {}

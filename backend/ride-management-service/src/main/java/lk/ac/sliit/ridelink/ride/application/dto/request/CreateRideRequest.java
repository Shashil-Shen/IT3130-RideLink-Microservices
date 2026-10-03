package lk.ac.sliit.ridelink.ride.application.dto.request;
import jakarta.validation.constraints.*; import lk.ac.sliit.ridelink.ride.domain.enums.VehicleType; import java.math.BigDecimal;
public record CreateRideRequest(@NotBlank @Size(max=160) String pickupName,@NotBlank @Size(max=160) String destinationName,@NotBlank @Size(max=80) String serviceArea,@NotNull @DecimalMin(value="0.1") @Digits(integer=8,fraction=2) BigDecimal simulatedDistanceKm,@NotNull VehicleType vehicleType) {}

package lk.ac.sliit.ridelink.ride.application.dto.response;
import lk.ac.sliit.ridelink.ride.domain.enums.VehicleType; import java.math.BigDecimal; import java.time.Instant; import java.util.UUID;
public record EligibleDriverData(UUID driverId,UUID driverAccountId,UUID vehicleId,VehicleType vehicleType,String serviceArea,BigDecimal latitude,BigDecimal longitude,Instant availableSince) {}

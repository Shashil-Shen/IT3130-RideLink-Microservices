package lk.ac.sliit.ridelink.ride.application.port.out;
import lk.ac.sliit.ridelink.ride.domain.enums.VehicleType; import java.math.BigDecimal;
public interface FareCalculationPort { BigDecimal estimate(VehicleType type,BigDecimal distanceKm); BigDecimal finalFare(VehicleType type,BigDecimal distanceKm); }

package lk.ac.sliit.ridelink.driver.domain.repository;
import lk.ac.sliit.ridelink.driver.domain.enums.VehicleType;
import lk.ac.sliit.ridelink.driver.domain.model.Vehicle;
import java.util.*;
public interface VehicleRepository {
 Vehicle save(Vehicle value); Optional<Vehicle> findById(UUID id); List<Vehicle> findByDriverId(UUID driverId);
 boolean existsByRegistrationNumber(String registration); boolean existsActiveByDriverId(UUID driverId);
 Optional<Vehicle> findFirstActiveMatching(UUID driverId, VehicleType type);
}

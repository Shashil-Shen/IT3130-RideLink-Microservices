package lk.ac.sliit.ridelink.driver.domain.repository;
import lk.ac.sliit.ridelink.driver.domain.enums.VehicleType;
import lk.ac.sliit.ridelink.driver.domain.model.DriverProfile;
import java.util.*;
public interface DriverProfileRepository {
 DriverProfile save(DriverProfile value); Optional<DriverProfile> findById(UUID id); Optional<DriverProfile> findByAccountId(UUID accountId);
 boolean existsByAccountId(UUID accountId); boolean existsByLicenceNumber(String licence); List<DriverProfile> findEligible(String serviceAreaKey, VehicleType type);
}

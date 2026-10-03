package lk.ac.sliit.ridelink.driver.application.port.in;
import lk.ac.sliit.ridelink.driver.application.dto.request.*; import lk.ac.sliit.ridelink.driver.application.dto.response.*; import lk.ac.sliit.ridelink.driver.domain.enums.*;
import java.util.*;
public interface DriverVehicleUseCase {
 DriverProfileResponse createProfile(UUID accountId,CreateDriverProfileRequest request); DriverProfileResponse getOwnProfile(UUID accountId);
 DriverProfileResponse getProfile(UUID driverId); DriverProfileResponse updateOwnProfile(UUID accountId,UpdateDriverProfileRequest request);
 DriverProfileResponse updateProfile(UUID driverId,UpdateDriverProfileRequest request);
 DriverProfileResponse updateAvailability(UUID accountId,AvailabilityStatus status); DriverProfileResponse updateLocation(UUID accountId,LocationRequest request);
 DriverProfileResponse updateServiceArea(UUID accountId,String serviceArea); VehicleResponse registerVehicle(UUID accountId,RegisterVehicleRequest request);
 List<VehicleResponse> getOwnVehicles(UUID accountId); VehicleResponse getVehicle(UUID requesterAccountId,boolean admin,UUID vehicleId);
 VehicleResponse updateVehicle(UUID requesterAccountId,boolean admin,UUID vehicleId,UpdateVehicleRequest request);
 VehicleResponse setVehicleActive(UUID requesterAccountId,boolean admin,UUID vehicleId,boolean active);
 List<EligibleDriverResponse> findEligible(String serviceArea,VehicleType type); DriverProfileResponse assign(UUID driverId,UUID rideId);
 DriverProfileResponse restore(UUID driverId,UUID rideId);
}

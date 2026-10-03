package lk.ac.sliit.ridelink.driver.application.mapper;
import lk.ac.sliit.ridelink.driver.application.dto.response.*; import lk.ac.sliit.ridelink.driver.domain.model.*; import java.util.List;
public final class DriverApplicationMapper {
 private DriverApplicationMapper(){}
 public static VehicleResponse vehicle(Vehicle v){return new VehicleResponse(v.getId(),v.getDriverId(),v.getRegistrationNumber(),v.getMake(),v.getModel(),v.getColour(),v.getVehicleType(),v.getManufacturingYear(),v.isActive(),v.getCreatedAt(),v.getUpdatedAt());}
 public static DriverProfileResponse driver(DriverProfile d,List<Vehicle> vehicles){return new DriverProfileResponse(d.getId(),d.getAccountId(),d.getLicenceNumber(),d.getAvailabilityStatus(),d.getServiceArea(),d.getCurrentLatitude(),d.getCurrentLongitude(),d.getAvailableSince(),d.getCurrentRideId(),d.getCreatedAt(),d.getUpdatedAt(),vehicles.stream().map(DriverApplicationMapper::vehicle).toList());}
}

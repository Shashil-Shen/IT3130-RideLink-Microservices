package lk.ac.sliit.ridelink.ride.application.port.out;
import lk.ac.sliit.ridelink.ride.application.dto.response.EligibleDriverData; import lk.ac.sliit.ridelink.ride.domain.enums.VehicleType; import java.util.*;
public interface DriverAssignmentPort { List<EligibleDriverData> eligible(String area,VehicleType type); void assign(UUID driverProfileId,UUID rideId); void restore(UUID driverProfileId,UUID rideId); }

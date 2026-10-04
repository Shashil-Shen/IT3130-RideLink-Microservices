package lk.ac.sliit.ridelink.ride.application.port.in;
import lk.ac.sliit.ridelink.ride.application.dto.response.RideResponse; import java.util.*;
public interface RideQueryUseCase { RideResponse get(UUID rideId,UUID requester,String role); List<RideResponse> passengerRides(UUID passengerId); List<RideResponse> driverRides(UUID driverId); }

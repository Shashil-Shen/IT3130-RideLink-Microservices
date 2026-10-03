package lk.ac.sliit.ridelink.ride.application.port.in;
import lk.ac.sliit.ridelink.ride.application.dto.request.CreateRideRequest; import lk.ac.sliit.ridelink.ride.application.dto.response.RideResponse; import java.util.UUID;
public interface RequestRideUseCase { RideResponse request(UUID passengerId,CreateRideRequest request); }

package lk.ac.sliit.ridelink.ride.application.port.in;
import lk.ac.sliit.ridelink.ride.application.dto.response.RideResponse; import java.util.UUID;
public interface RideLifecycleUseCase { RideResponse accept(UUID rideId,UUID driverId); RideResponse start(UUID rideId,UUID driverId); RideResponse complete(UUID rideId,UUID driverId); RideResponse cancelByPassenger(UUID rideId,UUID passengerId,String reason); RideResponse cancelByDriver(UUID rideId,UUID driverId,String reason); RideResponse retryDriverRelease(UUID rideId); }

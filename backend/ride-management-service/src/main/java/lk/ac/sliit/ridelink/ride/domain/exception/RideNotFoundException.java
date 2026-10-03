package lk.ac.sliit.ridelink.ride.domain.exception;
import java.util.UUID;
public class RideNotFoundException extends RuntimeException { public RideNotFoundException(UUID id){super("Ride not found: "+id);} }

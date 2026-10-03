package lk.ac.sliit.ridelink.driver.domain.exception;
import java.util.UUID;
public class VehicleNotFoundException extends RuntimeException { public VehicleNotFoundException(UUID id){super("Vehicle not found: "+id);} }

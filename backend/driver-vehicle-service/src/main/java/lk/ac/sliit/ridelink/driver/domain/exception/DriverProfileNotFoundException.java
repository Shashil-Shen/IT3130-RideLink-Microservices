package lk.ac.sliit.ridelink.driver.domain.exception;
import java.util.UUID;
public class DriverProfileNotFoundException extends RuntimeException { public DriverProfileNotFoundException(UUID id){super("Driver profile not found: "+id);} public static DriverProfileNotFoundException forAccount(UUID id){return new DriverProfileNotFoundException(id);} }

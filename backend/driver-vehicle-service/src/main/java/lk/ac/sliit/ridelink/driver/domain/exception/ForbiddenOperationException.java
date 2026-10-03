package lk.ac.sliit.ridelink.driver.domain.exception;
public class ForbiddenOperationException extends RuntimeException { public ForbiddenOperationException(){super("You do not own this driver or vehicle resource");} }

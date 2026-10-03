package lk.ac.sliit.ridelink.ride.application.usecase;
public class ExternalServiceException extends RuntimeException { private final boolean timeout; public ExternalServiceException(String message,boolean timeout,Throwable cause){super(message,cause);this.timeout=timeout;} public boolean isTimeout(){return timeout;} }

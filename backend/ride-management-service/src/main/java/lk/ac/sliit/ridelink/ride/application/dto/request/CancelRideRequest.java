package lk.ac.sliit.ridelink.ride.application.dto.request;
import jakarta.validation.constraints.*;
public record CancelRideRequest(@NotBlank @Size(max=500) String reason) {}

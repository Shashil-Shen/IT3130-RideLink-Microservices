package lk.ac.sliit.ridelink.driver.application.dto.request;
import jakarta.validation.constraints.NotNull; import lk.ac.sliit.ridelink.driver.domain.enums.AvailabilityStatus;
public record AvailabilityRequest(@NotNull AvailabilityStatus status) {}

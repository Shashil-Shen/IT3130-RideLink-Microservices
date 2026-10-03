package lk.ac.sliit.ridelink.driver.application.dto.request;
import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.Size;
public record ServiceAreaRequest(@NotBlank @Size(max=100) String serviceArea) {}

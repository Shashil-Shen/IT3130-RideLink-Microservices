package lk.ac.sliit.ridelink.driver.application.dto.request;
import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.Size;
public record CreateDriverProfileRequest(@NotBlank @Size(max=50) String licenceNumber,@NotBlank @Size(max=100) String serviceArea) {}

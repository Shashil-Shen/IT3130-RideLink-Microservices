package lk.ac.sliit.ridelink.account.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank @Size(max = 120) String fullName,
        @NotBlank @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$", message = "must be a valid international phone number") String phoneNumber
) {}

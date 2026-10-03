package lk.ac.sliit.ridelink.account.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterAccountRequest(
        @NotBlank @Size(max = 120) String fullName,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$", message = "must be a valid international phone number") String phoneNumber,
        @NotBlank @Size(min = 8, max = 72)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9])\\S+$",
                message = "must contain upper, lower, digit, special character and no spaces") String password
) {
    @Override
    public String toString() {
        return "RegisterAccountRequest[fullName=" + fullName + ", email=" + email
                + ", phoneNumber=" + phoneNumber + ", password=[REDACTED]]";
    }
}

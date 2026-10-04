package lk.ac.sliit.ridelink.account.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.ac.sliit.ridelink.account.application.dto.request.UpdateProfileRequest;
import lk.ac.sliit.ridelink.account.application.dto.response.AccountResponse;
import lk.ac.sliit.ridelink.account.application.port.in.ProfileUseCase;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts/me")
@Tag(name = "Profile", description = "Authenticated user's profile")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({@ApiResponse(responseCode = "400", description = "Request validation failed"),
        @ApiResponse(responseCode = "401", description = "Missing or invalid access token"),
        @ApiResponse(responseCode = "403", description = "Insufficient authority"),
        @ApiResponse(responseCode = "404", description = "Account not found")})
public class ProfileController 
{
    private final ProfileUseCase profiles;

    public ProfileController(ProfileUseCase profiles) { this.profiles = profiles; }

    @GetMapping
    @Operation(summary = "Retrieve the current user's profile")
    public AccountResponse get(Authentication authentication) {
        return profiles.getProfile(UUID.fromString(authentication.getName()));
    }

    @PatchMapping
    @Operation(summary = "Update the current user's name and phone number")
    public AccountResponse update(Authentication authentication, @Valid @RequestBody UpdateProfileRequest request) {
        return profiles.updateProfile(UUID.fromString(authentication.getName()), request);
    }
}

package lk.ac.sliit.ridelink.account.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.ac.sliit.ridelink.account.application.dto.request.LoginRequest;
import lk.ac.sliit.ridelink.account.application.dto.request.RegisterAccountRequest;
import lk.ac.sliit.ridelink.account.application.dto.response.AccountResponse;
import lk.ac.sliit.ridelink.account.application.dto.response.AuthenticationResponse;
import lk.ac.sliit.ridelink.account.application.port.in.LoginUseCase;
import lk.ac.sliit.ridelink.account.application.port.in.RegisterAccountUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Account registration and authentication")
@ApiResponses({@ApiResponse(responseCode = "400", description = "Request validation failed"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials or inactive account"),
        @ApiResponse(responseCode = "409", description = "Email already registered")})
public class AuthenticationController {
    private final RegisterAccountUseCase registration;
    private final LoginUseCase login;

    public AuthenticationController(RegisterAccountUseCase registration, LoginUseCase login) {
        this.registration = registration;
        this.login = login;
    }

    @PostMapping("/register/passengers")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a passenger account")
    public AccountResponse registerPassenger(@Valid @RequestBody RegisterAccountRequest request) {
        return registration.registerPassenger(request);
    }

    @PostMapping("/register/drivers")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a driver account")
    public AccountResponse registerDriver(@Valid @RequestBody RegisterAccountRequest request) {
        return registration.registerDriver(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate with email and password")
    public AuthenticationResponse login(@Valid @RequestBody LoginRequest request) {
        return login.login(request);
    }
}

package lk.ac.sliit.ridelink.ride.presentation.controller;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.ac.sliit.ridelink.ride.application.dto.request.*;
import lk.ac.sliit.ridelink.ride.application.dto.response.RideResponse;
import lk.ac.sliit.ridelink.ride.application.port.in.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/v1/rides")
@Tag(
        name = "Ride Management",
        description = "Manage ride requests, driver assignment, ride progress, completion and cancellation"
)
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Invalid UUID, JSON, enum or validation failure"),
        @ApiResponse(responseCode = "401", description = "Missing, expired or invalid JWT"),
        @ApiResponse(responseCode = "403", description = "Role, ownership or assigned-driver rule rejected the operation"),
        @ApiResponse(responseCode = "404", description = "Ride not found"),
        @ApiResponse(responseCode = "409", description = "Invalid lifecycle transition or concurrent update"),
        @ApiResponse(responseCode = "503", description = "No driver or downstream service unavailable"),
        @ApiResponse(responseCode = "504", description = "Downstream service timed out")
})
public class RideController {

    private final RequestRideUseCase requests;
    private final RideLifecycleUseCase lifecycle;
    private final RideQueryUseCase queries;

    public RideController(
            RequestRideUseCase r,
            RideLifecycleUseCase l,
            RideQueryUseCase q) {
        requests = r;
        lifecycle = l;
        queries = q;
    }

    @PostMapping
    @PreAuthorize("hasRole('PASSENGER')")
    @Operation(
            summary = "Request and assign a ride",
            description = "Allows a passenger to create a new ride request and receive a driver assignment"
    )
    @ApiResponse(responseCode = "201", description = "Ride assigned")
    public ResponseEntity<RideResponse> create(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateRideRequest q) {

        RideResponse r = requests.request(id(jwt), q);

        return ResponseEntity
                .created(URI.create("/api/v1/rides/" + r.id()))
                .body(r);
    }

    @GetMapping("/{rideId}")
    @PreAuthorize("hasAnyRole('PASSENGER','DRIVER','ADMIN')")
    public RideResponse get(
            @PathVariable UUID rideId,
            @AuthenticationPrincipal Jwt jwt) {

        return queries.get(
                rideId,
                id(jwt),
                jwt.getClaimAsString("role")
        );
    }

    @GetMapping("/passenger/me")
    @PreAuthorize("hasRole('PASSENGER')")
    public List<RideResponse> passenger(
            @AuthenticationPrincipal Jwt jwt) {

        return queries.passengerRides(id(jwt));
    }

    @GetMapping("/driver/me")
    @PreAuthorize("hasRole('DRIVER')")
    public List<RideResponse> driver(
            @AuthenticationPrincipal Jwt jwt) {

        return queries.driverRides(id(jwt));
    }

    @PostMapping("/{rideId}/accept")
    @PreAuthorize("hasRole('DRIVER')")
    public RideResponse accept(
            @PathVariable UUID rideId,
            @AuthenticationPrincipal Jwt jwt) {

        return lifecycle.accept(rideId, id(jwt));
    }

    @PostMapping("/{rideId}/start")
    @PreAuthorize("hasRole('DRIVER')")
    public RideResponse start(
            @PathVariable UUID rideId,
            @AuthenticationPrincipal Jwt jwt) {

        return lifecycle.start(rideId, id(jwt));
    }

    @PostMapping("/{rideId}/complete")
    @PreAuthorize("hasRole('DRIVER')")
    public RideResponse complete(
            @PathVariable UUID rideId,
            @AuthenticationPrincipal Jwt jwt) {

        return lifecycle.complete(rideId, id(jwt));
    }

    @PostMapping("/{rideId}/cancel")
    @PreAuthorize("hasAnyRole('PASSENGER','DRIVER')")
    public RideResponse cancel(
            @PathVariable UUID rideId,
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CancelRideRequest q) {

        return "PASSENGER".equals(jwt.getClaimAsString("role"))
                ? lifecycle.cancelByPassenger(rideId, id(jwt), q.reason())
                : lifecycle.cancelByDriver(rideId, id(jwt), q.reason());
    }

    private UUID id(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
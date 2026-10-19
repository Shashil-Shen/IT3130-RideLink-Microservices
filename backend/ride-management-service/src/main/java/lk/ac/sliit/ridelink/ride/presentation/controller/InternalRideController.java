package lk.ac.sliit.ridelink.ride.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lk.ac.sliit.ridelink.ride.application.dto.response.RideResponse;
import lk.ac.sliit.ridelink.ride.application.port.in.RideLifecycleUseCase;
import lk.ac.sliit.ridelink.ride.application.port.in.RideQueryUseCase;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/internal/rides")
@Tag(
        name = "Internal Ride Recovery",
        description = "Internal endpoints used for ride recovery and synchronization operations"
)
@SecurityRequirement(name = "bearerAuth")
public class InternalRideController {

    private final RideLifecycleUseCase rides;
    private final RideQueryUseCase queries;

    public InternalRideController(RideLifecycleUseCase rides, RideQueryUseCase queries) {
        this.rides = rides;
        this.queries = queries;
    }

    @GetMapping("/{rideId}")
    @PreAuthorize("hasAnyRole('SERVICE','ADMIN')")
    public RideResponse get(@PathVariable UUID rideId) {
        return queries.get(rideId, rideId, "ADMIN");
    }

    @PostMapping("/{rideId}/driver-sync")
    @PreAuthorize("hasAnyRole('SERVICE','ADMIN')")
    @Operation(
            summary = "Retry driver release synchronization",
            description = "Retries the driver release process for a ride. Accessible only by SERVICE or ADMIN roles."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Driver synchronization completed successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication is required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User does not have permission to perform this operation"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ride not found"
            )
    })
    public RideResponse retryDriverSync(@PathVariable UUID rideId) {
        return rides.retryDriverRelease(rideId);
    }
}

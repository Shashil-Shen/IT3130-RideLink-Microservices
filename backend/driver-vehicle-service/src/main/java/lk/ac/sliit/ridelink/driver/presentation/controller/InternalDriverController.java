package lk.ac.sliit.ridelink.driver.presentation.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lk.ac.sliit.ridelink.driver.application.dto.response.DriverProfileResponse;
import lk.ac.sliit.ridelink.driver.application.dto.response.EligibleDriverResponse;
import lk.ac.sliit.ridelink.driver.application.dto.response.VehicleResponse;
import lk.ac.sliit.ridelink.driver.application.port.in.DriverVehicleUseCase;
import lk.ac.sliit.ridelink.driver.domain.enums.VehicleType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/internal/drivers")
@Tag(name = "Internal driver contract")
@SecurityRequirement(name = "bearerAuth")
public class InternalDriverController {
    private final DriverVehicleUseCase service;

    public InternalDriverController(DriverVehicleUseCase service) { this.service = service; }

    @GetMapping("/{driverId}")
    public DriverProfileResponse getDriver(@PathVariable UUID driverId) { return service.getProfile(driverId); }

    @GetMapping("/vehicles/{vehicleId}")
    public VehicleResponse getVehicle(@PathVariable UUID vehicleId) { return service.getVehicleInternal(vehicleId); }

    @GetMapping("/eligible")
    public List<EligibleDriverResponse> eligible(@RequestParam String serviceArea, @RequestParam VehicleType vehicleType) {
        return service.findEligible(serviceArea, vehicleType);
    }

    @PutMapping("/{driverId}/assignments/{rideId}")
    public DriverProfileResponse assign(@PathVariable UUID driverId, @PathVariable UUID rideId) {
        return service.assign(driverId, rideId);
    }

    @DeleteMapping("/{driverId}/assignments/{rideId}")
    public DriverProfileResponse restore(@PathVariable UUID driverId, @PathVariable UUID rideId) {
        return service.restore(driverId, rideId);
    }
}

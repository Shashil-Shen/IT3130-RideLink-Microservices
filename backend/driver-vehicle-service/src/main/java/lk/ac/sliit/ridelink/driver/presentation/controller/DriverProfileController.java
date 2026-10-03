package lk.ac.sliit.ridelink.driver.presentation.controller;
import io.swagger.v3.oas.annotations.*; import io.swagger.v3.oas.annotations.security.SecurityRequirement; import io.swagger.v3.oas.annotations.tags.Tag; import jakarta.validation.Valid; import lk.ac.sliit.ridelink.driver.application.dto.request.*; import lk.ac.sliit.ridelink.driver.application.dto.response.*; import lk.ac.sliit.ridelink.driver.application.port.in.DriverVehicleUseCase; import org.springframework.http.HttpStatus; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/v1/drivers/me") @Tag(name="Driver profile") @SecurityRequirement(name="bearerAuth")
public class DriverProfileController { private final DriverVehicleUseCase service; public DriverProfileController(DriverVehicleUseCase s){service=s;} private UUID account(Authentication a){return UUID.fromString(a.getName());}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) @Operation(summary="Create the authenticated driver's operational profile") public DriverProfileResponse create(Authentication a,@Valid @RequestBody CreateDriverProfileRequest r){return service.createProfile(account(a),r);}
 @GetMapping public DriverProfileResponse get(Authentication a){return service.getOwnProfile(account(a));}
 @PatchMapping public DriverProfileResponse update(Authentication a,@Valid @RequestBody UpdateDriverProfileRequest r){return service.updateOwnProfile(account(a),r);}
 @PutMapping("/availability") public DriverProfileResponse availability(Authentication a,@Valid @RequestBody AvailabilityRequest r){return service.updateAvailability(account(a),r.status());}
 @PutMapping("/location") public DriverProfileResponse location(Authentication a,@Valid @RequestBody LocationRequest r){return service.updateLocation(account(a),r);}
 @PutMapping("/service-area") public DriverProfileResponse area(Authentication a,@Valid @RequestBody ServiceAreaRequest r){return service.updateServiceArea(account(a),r.serviceArea());}
 @PostMapping("/vehicles") @ResponseStatus(HttpStatus.CREATED) public VehicleResponse vehicle(Authentication a,@Valid @RequestBody RegisterVehicleRequest r){return service.registerVehicle(account(a),r);}
 @GetMapping("/vehicles") public List<VehicleResponse> vehicles(Authentication a){return service.getOwnVehicles(account(a));}
}

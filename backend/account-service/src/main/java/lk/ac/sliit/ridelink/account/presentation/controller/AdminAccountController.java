package lk.ac.sliit.ridelink.account.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.ac.sliit.ridelink.account.application.dto.request.UpdateAccountStatusRequest;
import lk.ac.sliit.ridelink.account.application.dto.request.UpdateAccountRoleRequest;
import lk.ac.sliit.ridelink.account.application.dto.response.AccountResponse;
import lk.ac.sliit.ridelink.account.application.port.in.AccountAdministrationUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/accounts")
@Tag(name = "Account administration")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({@ApiResponse(responseCode = "400", description = "Invalid UUID, status, role, or transition"),
        @ApiResponse(responseCode = "401", description = "Missing or invalid access token"),
        @ApiResponse(responseCode = "403", description = "ADMIN role required"),
        @ApiResponse(responseCode = "404", description = "Account not found")})
public class AdminAccountController 
{
    private final AccountAdministrationUseCase administration;

    public AdminAccountController(AccountAdministrationUseCase administration) { this.administration = administration; }

    @PatchMapping("/{accountId}/status")
    @Operation(summary = "Change an account's status", description = "Required role: ADMIN")
    public AccountResponse updateStatus(@PathVariable UUID accountId,
                                        @Valid @RequestBody UpdateAccountStatusRequest request) {
        return administration.updateStatus(accountId, request.status());
    }

    @PatchMapping("/{accountId}/role")
    @Operation(summary = "Change a passenger or driver role",
            description = "Required role: ADMIN. Allows PASSENGER and DRIVER transitions only; ADMIN provisioning is controlled separately.")
    public AccountResponse updateRole(@PathVariable UUID accountId,
                                      @Valid @RequestBody UpdateAccountRoleRequest request) {
        return administration.updateRole(accountId, request.role());
    }
}

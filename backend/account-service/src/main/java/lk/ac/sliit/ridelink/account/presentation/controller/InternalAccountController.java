package lk.ac.sliit.ridelink.account.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lk.ac.sliit.ridelink.account.application.dto.response.AccountResponse;
import lk.ac.sliit.ridelink.account.application.dto.response.AccountValidationResponse;
import lk.ac.sliit.ridelink.account.application.port.in.ValidateAccountUseCase;
import lk.ac.sliit.ridelink.account.domain.enums.AccountRole;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/internal/accounts")
@Tag(name = "Internal account contract",
        description = "Requires a signed token with token_type=SERVICE and role=SERVICE, or an ADMIN user token")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({@ApiResponse(responseCode = "400", description = "Invalid UUID or role parameter"),
        @ApiResponse(responseCode = "401", description = "Missing or invalid access token"),
        @ApiResponse(responseCode = "403", description = "SERVICE or ADMIN authority required"),
        @ApiResponse(responseCode = "404", description = "Account not found")})
public class InternalAccountController {
    private final ValidateAccountUseCase accounts;

    public InternalAccountController(ValidateAccountUseCase accounts) { this.accounts = accounts; }

    @GetMapping("/{accountId}")
    @Operation(summary = "Retrieve an account for authorized service use", description = "Required role: SERVICE or ADMIN")
    public AccountResponse get(@PathVariable UUID accountId) {
        return accounts.getAccount(accountId);
    }

    @GetMapping("/{accountId}/validation")
    @Operation(summary = "Validate account existence, active status and optional role",
            description = "Required role: SERVICE or ADMIN")
    public AccountValidationResponse validate(@PathVariable UUID accountId,
                                              @RequestParam(required = false) AccountRole requiredRole) {
        return accounts.validate(accountId, requiredRole);
    }
}

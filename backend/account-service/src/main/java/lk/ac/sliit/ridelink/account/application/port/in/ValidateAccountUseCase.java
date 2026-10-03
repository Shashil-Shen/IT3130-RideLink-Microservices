package lk.ac.sliit.ridelink.account.application.port.in;

import lk.ac.sliit.ridelink.account.application.dto.response.AccountResponse;
import lk.ac.sliit.ridelink.account.application.dto.response.AccountValidationResponse;
import lk.ac.sliit.ridelink.account.domain.enums.AccountRole;

import java.util.UUID;

public interface ValidateAccountUseCase {
    AccountResponse getAccount(UUID accountId);
    AccountValidationResponse validate(UUID accountId, AccountRole requiredRole);
}

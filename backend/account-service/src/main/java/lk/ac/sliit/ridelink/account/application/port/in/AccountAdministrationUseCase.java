package lk.ac.sliit.ridelink.account.application.port.in;

import lk.ac.sliit.ridelink.account.application.dto.response.AccountResponse;
import lk.ac.sliit.ridelink.account.domain.enums.AccountRole;
import lk.ac.sliit.ridelink.account.domain.enums.AccountStatus;

import java.util.UUID;

public interface AccountAdministrationUseCase 
{
    AccountResponse updateStatus(UUID accountId, AccountStatus status);
    AccountResponse updateRole(UUID accountId, AccountRole role);
}

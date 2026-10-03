package lk.ac.sliit.ridelink.account.application.dto.response;

import lk.ac.sliit.ridelink.account.domain.enums.AccountRole;
import lk.ac.sliit.ridelink.account.domain.enums.AccountStatus;

import java.util.UUID;

public record AccountValidationResponse(UUID accountId, AccountRole role, AccountStatus status, boolean valid) {}

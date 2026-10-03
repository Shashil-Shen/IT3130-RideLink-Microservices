package lk.ac.sliit.ridelink.account.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lk.ac.sliit.ridelink.account.domain.enums.AccountStatus;

public record UpdateAccountStatusRequest(@NotNull AccountStatus status) {}

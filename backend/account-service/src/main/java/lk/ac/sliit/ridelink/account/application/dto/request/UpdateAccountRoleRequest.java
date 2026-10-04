package lk.ac.sliit.ridelink.account.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lk.ac.sliit.ridelink.account.domain.enums.AccountRole;

public record UpdateAccountRoleRequest(@NotNull AccountRole role)
 {}

package lk.ac.sliit.ridelink.account.application.dto.response;

import lk.ac.sliit.ridelink.account.domain.enums.AccountRole;
import lk.ac.sliit.ridelink.account.domain.enums.AccountStatus;

import java.time.Instant;
import java.util.UUID;

public record AccountResponse
(UUID id, String fullName, String email, String phoneNumber,
                              AccountRole role, AccountStatus status, Instant createdAt, Instant updatedAt) {}

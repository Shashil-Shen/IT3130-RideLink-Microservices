package lk.ac.sliit.ridelink.account.domain.model;

import lk.ac.sliit.ridelink.account.domain.enums.AccountRole;
import lk.ac.sliit.ridelink.account.domain.enums.AccountStatus;

import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public final class Account {
    private final UUID id;
    private String fullName;
    private final String email;
    private String phoneNumber;
    private final String passwordHash;
    private AccountRole role;
    private AccountStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    public Account(UUID id, String fullName, String email, String phoneNumber, String passwordHash,
                   AccountRole role, AccountStatus status, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.fullName = requireText(fullName, "fullName");
        this.email = normalizeEmail(email);
        this.phoneNumber = requireText(phoneNumber, "phoneNumber");
        this.passwordHash = requireText(passwordHash, "passwordHash");
        this.role = Objects.requireNonNull(role);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static Account register(String fullName, String email, String phoneNumber, String passwordHash,
                                   AccountRole role) {
        Instant now = Instant.now();
        return new Account(UUID.randomUUID(), fullName, email, phoneNumber, passwordHash,
                role, AccountStatus.ACTIVE, now, now);
    }

    public void updateProfile(String fullName, String phoneNumber) {
        this.fullName = requireText(fullName, "fullName");
        this.phoneNumber = requireText(phoneNumber, "phoneNumber");
        this.updatedAt = Instant.now();
    }

    public void changeStatus(AccountStatus status) {
        this.status = Objects.requireNonNull(status);
        this.updatedAt = Instant.now();
    }

    public void changeRole(AccountRole newRole) {
        Objects.requireNonNull(newRole);
        if (role == AccountRole.ADMIN || newRole == AccountRole.ADMIN) {
            throw new IllegalArgumentException("ADMIN role cannot be assigned or removed through the API");
        }
        this.role = newRole;
        this.updatedAt = Instant.now();
    }

    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }

    public static String normalizeEmail(String value) {
        return requireText(value, "email").trim().toLowerCase(Locale.ROOT);
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    public UUID getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPasswordHash() { return passwordHash; }
    public AccountRole getRole() { return role; }
    public AccountStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}

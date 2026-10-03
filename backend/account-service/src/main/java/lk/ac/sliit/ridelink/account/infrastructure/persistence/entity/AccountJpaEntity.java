package lk.ac.sliit.ridelink.account.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lk.ac.sliit.ridelink.account.domain.enums.AccountRole;
import lk.ac.sliit.ridelink.account.domain.enums.AccountStatus;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts", indexes = {
        @Index(name = "idx_accounts_role_status", columnList = "role,status")
})
public class AccountJpaEntity {
    @Id
    private UUID id;
    @Column(name = "full_name", nullable = false, length = 120)
    private String fullName;
    @Column(nullable = false, unique = true, length = 254)
    private String email;
    @Column(name = "phone_number", nullable = false, length = 16)
    private String phoneNumber;
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountRole role;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    protected AccountJpaEntity() {}

    public AccountJpaEntity(UUID id, String fullName, String email, String phoneNumber, String passwordHash,
                            AccountRole role, AccountStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id; this.fullName = fullName; this.email = email; this.phoneNumber = phoneNumber;
        this.passwordHash = passwordHash; this.role = role; this.status = status;
        this.createdAt = createdAt; this.updatedAt = updatedAt;
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

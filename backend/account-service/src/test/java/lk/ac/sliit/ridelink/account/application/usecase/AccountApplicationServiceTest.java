package lk.ac.sliit.ridelink.account.application.usecase;

import lk.ac.sliit.ridelink.account.application.dto.request.LoginRequest;
import lk.ac.sliit.ridelink.account.application.dto.request.RegisterAccountRequest;
import lk.ac.sliit.ridelink.account.application.dto.request.UpdateProfileRequest;
import lk.ac.sliit.ridelink.account.application.port.out.PasswordPort;
import lk.ac.sliit.ridelink.account.application.port.out.TokenGenerationPort;
import lk.ac.sliit.ridelink.account.domain.enums.AccountRole;
import lk.ac.sliit.ridelink.account.domain.enums.AccountStatus;
import lk.ac.sliit.ridelink.account.domain.exception.DuplicateEmailException;
import lk.ac.sliit.ridelink.account.domain.exception.InactiveAccountException;
import lk.ac.sliit.ridelink.account.domain.exception.InvalidCredentialsException;
import lk.ac.sliit.ridelink.account.domain.model.Account;
import lk.ac.sliit.ridelink.account.domain.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountApplicationServiceTest {
    @Mock AccountRepository repository;
    @Mock PasswordPort passwords;
    @Mock TokenGenerationPort tokens;
    AccountApplicationService service;

    @BeforeEach
    void setUp() {
        service = new AccountApplicationService(repository, passwords, tokens);
    }

    @Test
    void registersPassengerWithNormalizedEmailAndHashedPassword() {
        var request = request("Passenger@Test.COM");
        when(passwords.hash("Strong@123")).thenReturn("bcrypt-hash");
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.registerPassenger(request);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getPasswordHash()).isEqualTo("bcrypt-hash");
        assertThat(response.email()).isEqualTo("passenger@test.com");
        assertThat(response.role()).isEqualTo(AccountRole.PASSENGER);
        assertThat(response.status()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(request.toString()).doesNotContain("Strong@123").contains("[REDACTED]");
        assertThat(new LoginRequest("user@example.test", "Strong@123").toString())
                .doesNotContain("Strong@123").contains("[REDACTED]");
    }

    @Test
    void registersDriverWithDriverRole() {
        when(passwords.hash(anyString())).thenReturn("hash");
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        assertThat(service.registerDriver(request("driver@example.test")).role()).isEqualTo(AccountRole.DRIVER);
    }

    @Test
    void rejectsDuplicateEmailCaseInsensitively() {
        when(repository.existsByEmail("passenger@test.com")).thenReturn(true);
        assertThatThrownBy(() -> service.registerPassenger(request("Passenger@Test.com")))
                .isInstanceOf(DuplicateEmailException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void loginReturnsTokenForActiveAccount() {
        Account account = account(AccountStatus.ACTIVE);
        when(repository.findByEmail(account.getEmail())).thenReturn(Optional.of(account));
        when(passwords.matches("Strong@123", "hash")).thenReturn(true);
        when(tokens.generate(account)).thenReturn("jwt");
        when(tokens.expirationMs()).thenReturn(3600000L);

        var response = service.login(new LoginRequest(account.getEmail(), "Strong@123"));
        assertThat(response.accessToken()).isEqualTo("jwt");
        assertThat(response.account().email()).isEqualTo(account.getEmail());
    }

    @Test
    void rejectsInvalidPassword() {
        Account account = account(AccountStatus.ACTIVE);
        when(repository.findByEmail(account.getEmail())).thenReturn(Optional.of(account));
        when(passwords.matches(anyString(), anyString())).thenReturn(false);
        assertThatThrownBy(() -> service.login(new LoginRequest(account.getEmail(), "wrong")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void rejectsInactiveLogin() {
        Account account = account(AccountStatus.INACTIVE);
        when(repository.findByEmail(account.getEmail())).thenReturn(Optional.of(account));
        when(passwords.matches(anyString(), anyString())).thenReturn(true);
        assertThatThrownBy(() -> service.login(new LoginRequest(account.getEmail(), "Strong@123")))
                .isInstanceOf(InactiveAccountException.class);
    }

    @Test
    void rejectsSuspendedLogin() {
        Account account = account(AccountStatus.SUSPENDED);
        when(repository.findByEmail(account.getEmail())).thenReturn(Optional.of(account));
        when(passwords.matches(anyString(), anyString())).thenReturn(true);
        assertThatThrownBy(() -> service.login(new LoginRequest(account.getEmail(), "Strong@123")))
                .isInstanceOf(InactiveAccountException.class);
    }

    @Test
    void retrievesAndUpdatesOwnProfileWithoutChangingRole() {
        Account account = account(AccountStatus.ACTIVE);
        when(repository.findById(account.getId())).thenReturn(Optional.of(account));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(service.getProfile(account.getId()).email()).isEqualTo(account.getEmail());
        var updated = service.updateProfile(account.getId(), new UpdateProfileRequest("New Name", "+94771234568"));
        assertThat(updated.fullName()).isEqualTo("New Name");
        assertThat(updated.role()).isEqualTo(AccountRole.PASSENGER);
    }

    @Test
    void administratorCanUpdateStatusAndValidationReflectsIt() {
        Account account = account(AccountStatus.ACTIVE);
        when(repository.findById(account.getId())).thenReturn(Optional.of(account));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(service.updateStatus(account.getId(), AccountStatus.SUSPENDED).status())
                .isEqualTo(AccountStatus.SUSPENDED);
        assertThat(service.validate(account.getId(), AccountRole.PASSENGER).valid()).isFalse();
    }

    private RegisterAccountRequest request(String email) {
        return new RegisterAccountRequest("Test User", email, "+94771234567", "Strong@123");
    }

    private Account account(AccountStatus status) {
        Instant now = Instant.now();
        return new Account(UUID.randomUUID(), "Test User", "passenger@example.test", "+94771234567",
                "hash", AccountRole.PASSENGER, status, now, now);
    }
}

package lk.ac.sliit.ridelink.account.application.usecase;

import lk.ac.sliit.ridelink.account.application.dto.request.LoginRequest;
import lk.ac.sliit.ridelink.account.application.dto.request.RegisterAccountRequest;
import lk.ac.sliit.ridelink.account.application.dto.request.UpdateProfileRequest;
import lk.ac.sliit.ridelink.account.application.dto.response.AccountResponse;
import lk.ac.sliit.ridelink.account.application.dto.response.AccountValidationResponse;
import lk.ac.sliit.ridelink.account.application.dto.response.AuthenticationResponse;
import lk.ac.sliit.ridelink.account.application.mapper.AccountApplicationMapper;
import lk.ac.sliit.ridelink.account.application.port.in.AccountAdministrationUseCase;
import lk.ac.sliit.ridelink.account.application.port.in.LoginUseCase;
import lk.ac.sliit.ridelink.account.application.port.in.ProfileUseCase;
import lk.ac.sliit.ridelink.account.application.port.in.RegisterAccountUseCase;
import lk.ac.sliit.ridelink.account.application.port.in.ValidateAccountUseCase;
import lk.ac.sliit.ridelink.account.application.port.out.PasswordPort;
import lk.ac.sliit.ridelink.account.application.port.out.TokenGenerationPort;
import lk.ac.sliit.ridelink.account.domain.enums.AccountRole;
import lk.ac.sliit.ridelink.account.domain.enums.AccountStatus;
import lk.ac.sliit.ridelink.account.domain.exception.AccountNotFoundException;
import lk.ac.sliit.ridelink.account.domain.exception.DuplicateEmailException;
import lk.ac.sliit.ridelink.account.domain.exception.InvalidCredentialsException;
import lk.ac.sliit.ridelink.account.domain.model.Account;
import lk.ac.sliit.ridelink.account.domain.repository.AccountRepository;
import lk.ac.sliit.ridelink.account.domain.service.AccountAccessPolicy;

import java.util.UUID;

public class AccountApplicationService implements RegisterAccountUseCase, LoginUseCase, ProfileUseCase,
        AccountAdministrationUseCase, ValidateAccountUseCase {
    private final AccountRepository repository;
    private final PasswordPort passwordPort;
    private final TokenGenerationPort tokenPort;

    public AccountApplicationService(AccountRepository repository, PasswordPort passwordPort,
                                     TokenGenerationPort tokenPort) {
        this.repository = repository;
        this.passwordPort = passwordPort;
        this.tokenPort = tokenPort;
    }

    @Override
    public AccountResponse registerPassenger(RegisterAccountRequest request) {
        return register(request, AccountRole.PASSENGER);
    }

    @Override
    public AccountResponse registerDriver(RegisterAccountRequest request) {
        return register(request, AccountRole.DRIVER);
    }

    private AccountResponse register(RegisterAccountRequest request, AccountRole role) {
        String email = Account.normalizeEmail(request.email());
        if (repository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
        Account saved = repository.save(Account.register(request.fullName(), email, request.phoneNumber(),
                passwordPort.hash(request.password()), role));
        return AccountApplicationMapper.toResponse(saved);
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        Account account = repository.findByEmail(Account.normalizeEmail(request.email()))
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordPort.matches(request.password(), account.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        AccountAccessPolicy.requireActive(account);
        return new AuthenticationResponse(tokenPort.generate(account), "Bearer", tokenPort.expirationMs(),
                AccountApplicationMapper.toResponse(account));
    }

    @Override
    public AccountResponse getProfile(UUID accountId) {
        return AccountApplicationMapper.toResponse(load(accountId));
    }

    @Override
    public AccountResponse updateProfile(UUID accountId, UpdateProfileRequest request) {
        Account account = load(accountId);
        account.updateProfile(request.fullName(), request.phoneNumber());
        return AccountApplicationMapper.toResponse(repository.save(account));
    }

    @Override
    public AccountResponse updateStatus(UUID accountId, AccountStatus status) {
        Account account = load(accountId);
        account.changeStatus(status);
        return AccountApplicationMapper.toResponse(repository.save(account));
    }

    @Override
    public AccountResponse updateRole(UUID accountId, AccountRole role) {
        Account account = load(accountId);
        account.changeRole(role);
        return AccountApplicationMapper.toResponse(repository.save(account));
    }

    @Override
    public AccountResponse getAccount(UUID accountId) {
        return AccountApplicationMapper.toResponse(load(accountId));
    }

    @Override
    public AccountValidationResponse validate(UUID accountId, AccountRole requiredRole) {
        Account account = load(accountId);
        boolean valid = account.isActive() && (requiredRole == null || account.getRole() == requiredRole);
        return new AccountValidationResponse(account.getId(), account.getRole(), account.getStatus(), valid);
    }

    private Account load(UUID id) {
        return repository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }
}

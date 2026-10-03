package lk.ac.sliit.ridelink.account.infrastructure.persistence.adapter;

import lk.ac.sliit.ridelink.account.domain.model.Account;
import lk.ac.sliit.ridelink.account.domain.repository.AccountRepository;
import lk.ac.sliit.ridelink.account.infrastructure.persistence.mapper.AccountPersistenceMapper;
import lk.ac.sliit.ridelink.account.infrastructure.persistence.repository.SpringDataAccountRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AccountPersistenceAdapter implements AccountRepository {
    private final SpringDataAccountRepository repository;

    public AccountPersistenceAdapter(SpringDataAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account save(Account account) {
        return AccountPersistenceMapper.toDomain(repository.save(AccountPersistenceMapper.toEntity(account)));
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return repository.findById(id).map(AccountPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Account> findByEmail(String normalizedEmail) {
        return repository.findByEmailIgnoreCase(normalizedEmail).map(AccountPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String normalizedEmail) {
        return repository.existsByEmailIgnoreCase(normalizedEmail);
    }
}

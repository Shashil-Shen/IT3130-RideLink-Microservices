package lk.ac.sliit.ridelink.account.domain.repository;

import lk.ac.sliit.ridelink.account.domain.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository 
{
    Account save(Account account);
    Optional<Account> findById(UUID id);
    Optional<Account> findByEmail(String normalizedEmail);
    boolean existsByEmail(String normalizedEmail);
}

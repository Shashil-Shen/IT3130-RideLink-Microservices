package lk.ac.sliit.ridelink.account.infrastructure.persistence.mapper;

import lk.ac.sliit.ridelink.account.domain.model.Account;
import lk.ac.sliit.ridelink.account.infrastructure.persistence.entity.AccountJpaEntity;

public final class AccountPersistenceMapper 
{
    private AccountPersistenceMapper() {}

    public static AccountJpaEntity toEntity(Account account) {
        return new AccountJpaEntity(account.getId(), account.getFullName(), account.getEmail(),
                account.getPhoneNumber(), account.getPasswordHash(), account.getRole(), account.getStatus(),
                account.getCreatedAt(), account.getUpdatedAt());
    }

    public static Account toDomain(AccountJpaEntity entity) {
        return new Account(entity.getId(), entity.getFullName(), entity.getEmail(), entity.getPhoneNumber(),
                entity.getPasswordHash(), entity.getRole(), entity.getStatus(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}

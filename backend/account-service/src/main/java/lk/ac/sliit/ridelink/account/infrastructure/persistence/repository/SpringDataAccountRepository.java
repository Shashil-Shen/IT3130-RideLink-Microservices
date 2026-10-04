package lk.ac.sliit.ridelink.account.infrastructure.persistence.repository;

import lk.ac.sliit.ridelink.account.infrastructure.persistence.entity.AccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataAccountRepository extends JpaRepository<AccountJpaEntity, UUID> 
{
    Optional<AccountJpaEntity> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}

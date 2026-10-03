package lk.ac.sliit.ridelink.account.application.port.out;

import lk.ac.sliit.ridelink.account.domain.model.Account;

public interface TokenGenerationPort {
    String generate(Account account);
    long expirationMs();
}

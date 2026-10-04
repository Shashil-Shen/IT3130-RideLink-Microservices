package lk.ac.sliit.ridelink.account.domain.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException 
{
    public AccountNotFoundException(UUID id) {
        super("Account not found: " + id);
    }
}

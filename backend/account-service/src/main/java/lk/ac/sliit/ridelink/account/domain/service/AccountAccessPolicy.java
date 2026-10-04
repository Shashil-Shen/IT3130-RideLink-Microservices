package lk.ac.sliit.ridelink.account.domain.service;

import lk.ac.sliit.ridelink.account.domain.exception.InactiveAccountException;
import lk.ac.sliit.ridelink.account.domain.model.Account;

public final class AccountAccessPolicy 
{
    private AccountAccessPolicy() {}

    public static void requireActive(Account account) {
        if (!account.isActive()) {
            throw new InactiveAccountException();
        }
    }
}

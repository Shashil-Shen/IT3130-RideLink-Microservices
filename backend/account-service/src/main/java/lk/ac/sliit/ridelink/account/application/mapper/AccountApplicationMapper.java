package lk.ac.sliit.ridelink.account.application.mapper;

import lk.ac.sliit.ridelink.account.application.dto.response.AccountResponse;
import lk.ac.sliit.ridelink.account.domain.model.Account;

public final class AccountApplicationMapper {
    private AccountApplicationMapper() {}

    public static AccountResponse toResponse(Account account) 
    {
        return new AccountResponse(account.getId(), account.getFullName(), account.getEmail(),
                account.getPhoneNumber(), account.getRole(), account.getStatus(),
                account.getCreatedAt(), account.getUpdatedAt());
    }
}

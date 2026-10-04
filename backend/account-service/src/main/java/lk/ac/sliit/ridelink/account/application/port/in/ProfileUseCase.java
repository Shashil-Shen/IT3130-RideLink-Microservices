package lk.ac.sliit.ridelink.account.application.port.in;

import lk.ac.sliit.ridelink.account.application.dto.request.UpdateProfileRequest;
import lk.ac.sliit.ridelink.account.application.dto.response.AccountResponse;

import java.util.UUID;

public interface ProfileUseCase 
{
    AccountResponse getProfile(UUID accountId);
    AccountResponse updateProfile(UUID accountId, UpdateProfileRequest request);
}

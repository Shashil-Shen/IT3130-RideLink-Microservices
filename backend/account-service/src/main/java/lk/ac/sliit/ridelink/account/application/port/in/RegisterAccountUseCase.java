package lk.ac.sliit.ridelink.account.application.port.in;

import lk.ac.sliit.ridelink.account.application.dto.request.RegisterAccountRequest;
import lk.ac.sliit.ridelink.account.application.dto.response.AccountResponse;

public interface RegisterAccountUseCase {
    AccountResponse registerPassenger(RegisterAccountRequest request);
    AccountResponse registerDriver(RegisterAccountRequest request);
}

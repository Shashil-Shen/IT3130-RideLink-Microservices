package lk.ac.sliit.ridelink.account.application.port.in;

import lk.ac.sliit.ridelink.account.application.dto.request.LoginRequest;
import lk.ac.sliit.ridelink.account.application.dto.response.AuthenticationResponse;

public interface LoginUseCase {
    AuthenticationResponse login(LoginRequest request);
}

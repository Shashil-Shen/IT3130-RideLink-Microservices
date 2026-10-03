package lk.ac.sliit.ridelink.account.application.dto.response;

public record AuthenticationResponse(String accessToken, String tokenType, long expiresInMs,
                                     AccountResponse account) {}

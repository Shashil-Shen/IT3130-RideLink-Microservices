package com.ridelink.fare_payment_service.service;

import com.ridelink.fare_payment_service.dto.response.PassengerResponse;
import com.ridelink.fare_payment_service.dto.response.RideResponse;
import com.ridelink.fare_payment_service.dto.response.VehicleResponse;
import com.ridelink.fare_payment_service.enums.VehicleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Service
public class ExternalServiceClient {
    private final RestClient rideClient;
    private final RestClient accountClient;
    private final RestClient driverClient;
    private final InternalServiceTokenProvider tokens;

    public ExternalServiceClient(RestClient.Builder builder, InternalServiceTokenProvider tokens,
                                 @Value("${ridelink.ride-service-url}") String rideServiceUrl,
                                 @Value("${ridelink.account-service-url}") String accountServiceUrl,
                                 @Value("${ridelink.driver-service-url}") String driverServiceUrl) {
        this.rideClient = builder.clone().baseUrl(rideServiceUrl).build();
        this.accountClient = builder.clone().baseUrl(accountServiceUrl).build();
        this.driverClient = builder.clone().baseUrl(driverServiceUrl).build();
        this.tokens = tokens;
    }

    public RideResponse getRide(UUID rideId) {
        return get(rideClient, "/api/v1/internal/rides/{id}", RideResponse.class, rideId);
    }

    public PassengerResponse getPassenger(UUID passengerId) {
        AccountContract account = get(accountClient, "/api/v1/internal/accounts/{id}", AccountContract.class, passengerId);
        PassengerResponse response = new PassengerResponse();
        response.setId(account.id());
        response.setName(account.fullName());
        return response;
    }

    public VehicleResponse getVehicle(UUID vehicleId) {
        VehicleContract vehicle = get(driverClient, "/api/v1/internal/drivers/vehicles/{id}", VehicleContract.class, vehicleId);
        DriverContract driver = get(driverClient, "/api/v1/internal/drivers/{id}", DriverContract.class, vehicle.driverId());
        AccountContract owner = get(accountClient, "/api/v1/internal/accounts/{id}", AccountContract.class, driver.accountId());
        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.id());
        response.setVehicleNumber(vehicle.registrationNumber());
        response.setVehicleType(vehicle.vehicleType());
        response.setOwnerName(owner.fullName());
        return response;
    }

    private <T> T get(RestClient client, String uri, Class<T> type, Object id) {
        try {
            T body = client.get().uri(uri, id)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.token())
                    .retrieve().body(type);
            if (body == null) throw new IllegalStateException("Downstream service returned an empty response");
            return body;
        } catch (RestClientException exception) {
            throw new IllegalStateException("Downstream RideLink service is unavailable", exception);
        }
    }

    private record AccountContract(UUID id, String fullName) {}
    private record DriverContract(UUID id, UUID accountId) {}
    private record VehicleContract(UUID id, UUID driverId, String registrationNumber, VehicleType vehicleType) {}
}

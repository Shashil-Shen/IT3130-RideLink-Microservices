package com.ridelink.fare_payment_service.service;

import org.springframework.stereotype.Service;

import com.ridelink.fare_payment_service.dto.response.PassengerResponse;
import com.ridelink.fare_payment_service.dto.response.RideResponse;
import com.ridelink.fare_payment_service.dto.response.VehicleResponse;

@Service
public class ExternalServiceClient {

    public RideResponse getRide(Long rideId) {
        throw new UnsupportedOperationException(
                "Ride Management Service integration is not configured yet"
        );
    }

    public PassengerResponse getPassenger(Long passengerId) {
        throw new UnsupportedOperationException(
                "Account Service integration is not configured yet"
        );
    }

    public VehicleResponse getVehicle(Long vehicleId) {
        throw new UnsupportedOperationException(
                "Driver and Vehicle Service integration is not configured yet"
        );
    }
}
package com.ridelink.fare_payment_service.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.ridelink.fare_payment_service.dto.FareEstimateResponse;

@Service
public class FareCalculationService {

    private static final BigDecimal BASE_FARE =
            new BigDecimal("200.00");

    private static final BigDecimal PRICE_PER_KM =
            new BigDecimal("100.00");

    public FareEstimateResponse calculateFare(BigDecimal distanceKm) {

        BigDecimal distanceCharge =
                distanceKm.multiply(PRICE_PER_KM);

        BigDecimal estimatedFare =
                BASE_FARE.add(distanceCharge);

        return new FareEstimateResponse(
                distanceKm,
                BASE_FARE,
                PRICE_PER_KM,
                estimatedFare
        );
    }
}
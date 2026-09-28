package com.ridelink.fare_payment_service.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.ridelink.fare_payment_service.dto.response.FareEstimateResponse;
import com.ridelink.fare_payment_service.enums.VehicleType;

@Service
public class FareCalculationService {

    private static final BigDecimal DISCOUNT_DISTANCE_BLOCK =
            new BigDecimal("10");

    private static final BigDecimal DISCOUNT_PER_BLOCK =
            new BigDecimal("100.00");

    public FareEstimateResponse calculateFare(
            VehicleType vehicleType,
            BigDecimal distanceKm) {

        validateInput(vehicleType, distanceKm);

        BigDecimal baseFare =
                vehicleType.getBaseFare();

        BigDecimal pricePerKm =
                vehicleType.getPricePerKm();

        BigDecimal distanceCharge =
                distanceKm.multiply(pricePerKm);

        BigDecimal discount =
                calculateDistanceDiscount(distanceKm);

        BigDecimal totalFare =
                baseFare
                        .add(distanceCharge)
                        .subtract(discount);

        if (totalFare.compareTo(BigDecimal.ZERO) < 0) {
            totalFare = BigDecimal.ZERO;
        }

        return new FareEstimateResponse(
                vehicleType,
                distanceKm,
                baseFare,
                pricePerKm,
                distanceCharge,
                discount,
                totalFare
        );
    }

    private BigDecimal calculateDistanceDiscount(
            BigDecimal distanceKm) {

        // First 10 km receives no discount.
        if (distanceKm.compareTo(DISCOUNT_DISTANCE_BLOCK) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal completedBlocks =
                distanceKm.divide(
                        DISCOUNT_DISTANCE_BLOCK,
                        0,
                        RoundingMode.FLOOR
                );

        return completedBlocks.multiply(DISCOUNT_PER_BLOCK);
    }

    private void validateInput(
            VehicleType vehicleType,
            BigDecimal distanceKm) {

        if (vehicleType == null) {
            throw new IllegalArgumentException(
                    "Vehicle type is required"
            );
        }

        if (distanceKm == null ||
                distanceKm.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Distance must be greater than zero"
            );
        }
    }
}
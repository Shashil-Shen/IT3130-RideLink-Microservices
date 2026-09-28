package com.ridelink.fare_payment_service.dto;

import java.math.BigDecimal;

public class FareEstimateResponse {

    private BigDecimal distanceKm;
    private BigDecimal baseFare;
    private BigDecimal pricePerKm;
    private BigDecimal estimatedFare;

    public FareEstimateResponse(
            BigDecimal distanceKm,
            BigDecimal baseFare,
            BigDecimal pricePerKm,
            BigDecimal estimatedFare) {

        this.distanceKm = distanceKm;
        this.baseFare = baseFare;
        this.pricePerKm = pricePerKm;
        this.estimatedFare = estimatedFare;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public BigDecimal getBaseFare() {
        return baseFare;
    }

    public BigDecimal getPricePerKm() {
        return pricePerKm;
    }

    public BigDecimal getEstimatedFare() {
        return estimatedFare;
    }
}
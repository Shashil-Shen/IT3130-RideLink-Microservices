package com.ridelink.fare_payment_service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class FareEstimateRequest {

    @NotNull(message = "Distance is required")
    @DecimalMin(value = "0.1", message = "Distance must be greater than 0")
    private BigDecimal distanceKm;

    public FareEstimateRequest() {
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }
}
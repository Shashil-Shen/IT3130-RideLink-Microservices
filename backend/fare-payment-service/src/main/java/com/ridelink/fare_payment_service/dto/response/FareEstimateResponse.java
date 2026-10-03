package com.ridelink.fare_payment_service.dto.response;

import java.math.BigDecimal;

import com.ridelink.fare_payment_service.enums.VehicleType;

public class FareEstimateResponse {

    private VehicleType vehicleType;
    private BigDecimal distanceKm;

    private BigDecimal baseFare;
    private BigDecimal pricePerKm;
    private BigDecimal distanceCharge;
    private BigDecimal discount;
    private BigDecimal totalFare;

    public FareEstimateResponse(
            VehicleType vehicleType,
            BigDecimal distanceKm,
            BigDecimal baseFare,
            BigDecimal pricePerKm,
            BigDecimal distanceCharge,
            BigDecimal discount,
            BigDecimal totalFare) {

        this.vehicleType = vehicleType;
        this.distanceKm = distanceKm;
        this.baseFare = baseFare;
        this.pricePerKm = pricePerKm;
        this.distanceCharge = distanceCharge;
        this.discount = discount;
        this.totalFare = totalFare;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
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

    public BigDecimal getDistanceCharge() {
        return distanceCharge;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getTotalFare() {
        return totalFare;
    }
}
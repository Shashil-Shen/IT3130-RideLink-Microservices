package com.ridelink.fare_payment_service.enums;

import java.math.BigDecimal;

public enum VehicleType {

    BIKE("100.00", "80.00"),
    THREE_WHEELER("200.00", "100.00"),
    CAR("300.00", "150.00"),
    VAN("400.00", "200.00");

    private final BigDecimal baseFare;
    private final BigDecimal pricePerKm;

    VehicleType(String baseFare, String pricePerKm) {
        this.baseFare = new BigDecimal(baseFare);
        this.pricePerKm = new BigDecimal(pricePerKm);
    }

    public BigDecimal getBaseFare() {
        return baseFare;
    }

    public BigDecimal getPricePerKm() {
        return pricePerKm;
    }
}
package com.ridelink.fare_payment_service.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public class RideResponse {

    private UUID id;
    private UUID passengerId;
    private UUID vehicleId;
    private String pickupName;
    private String destinationName;
    private BigDecimal simulatedDistanceKm;
    private String status;

    public RideResponse() {
    }

    public UUID getRideId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(UUID passengerId) {
        this.passengerId = passengerId;
    }

    public UUID getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(UUID vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getJourneyStart() {
        return pickupName;
    }

    public void setPickupName(String pickupName) {
        this.pickupName = pickupName;
    }

    public String getJourneyEnd() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public BigDecimal getDistanceKm() {
        return simulatedDistanceKm;
    }

    public void setSimulatedDistanceKm(BigDecimal simulatedDistanceKm) {
        this.simulatedDistanceKm = simulatedDistanceKm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

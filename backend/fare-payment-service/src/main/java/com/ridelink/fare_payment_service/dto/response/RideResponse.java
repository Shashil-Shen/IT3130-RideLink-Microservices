package com.ridelink.fare_payment_service.dto.response;

import java.math.BigDecimal;

public class RideResponse {

    private Long rideId;
    private Long passengerId;
    private Long vehicleId;
    private String journeyStart;
    private String journeyEnd;
    private BigDecimal distanceKm;
    private String status;

    public RideResponse() {
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getJourneyStart() {
        return journeyStart;
    }

    public void setJourneyStart(String journeyStart) {
        this.journeyStart = journeyStart;
    }

    public String getJourneyEnd() {
        return journeyEnd;
    }

    public void setJourneyEnd(String journeyEnd) {
        this.journeyEnd = journeyEnd;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
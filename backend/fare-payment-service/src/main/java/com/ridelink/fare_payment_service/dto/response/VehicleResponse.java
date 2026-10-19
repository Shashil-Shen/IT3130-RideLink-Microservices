package com.ridelink.fare_payment_service.dto.response;

import com.ridelink.fare_payment_service.enums.VehicleType;
import java.util.UUID;

public class VehicleResponse {

    private UUID id;
    private String vehicleNumber;
    private VehicleType vehicleType;
    private String ownerName;

    public VehicleResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}

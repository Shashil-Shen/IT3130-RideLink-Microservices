package com.ridelink.fare_payment_service.dto.response;

import java.util.UUID;

public class PassengerResponse {

    private UUID id;
    private String name;

    public PassengerResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

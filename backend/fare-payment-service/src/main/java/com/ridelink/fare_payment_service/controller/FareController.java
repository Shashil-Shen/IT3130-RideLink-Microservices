package com.ridelink.fare_payment_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ridelink.fare_payment_service.dto.request.FareEstimateRequest;
import com.ridelink.fare_payment_service.dto.response.FareEstimateResponse;
import com.ridelink.fare_payment_service.service.FareCalculationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fares")
public class FareController {

    private final FareCalculationService fareCalculationService;

    public FareController(
            FareCalculationService fareCalculationService) {

        this.fareCalculationService = fareCalculationService;
    }

    @PostMapping("/estimate")
    public ResponseEntity<FareEstimateResponse> estimateFare(
            @Valid @RequestBody FareEstimateRequest request) {

        FareEstimateResponse response =
                fareCalculationService.calculateFare(
                        request.getVehicleType(),
                        request.getDistanceKm()
                );

        return ResponseEntity.ok(response);
    }
}
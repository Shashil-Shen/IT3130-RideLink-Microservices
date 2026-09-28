package com.ridelink.fare_payment_service.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ridelink.fare_payment_service.dto.request.PaymentRequest;
import com.ridelink.fare_payment_service.dto.response.FareEstimateResponse;
import com.ridelink.fare_payment_service.dto.response.PaymentResponse;
import com.ridelink.fare_payment_service.dto.response.RideResponse;
import com.ridelink.fare_payment_service.dto.response.VehicleResponse;
import com.ridelink.fare_payment_service.entity.Payment;
import com.ridelink.fare_payment_service.enums.PaymentStatus;
import com.ridelink.fare_payment_service.exception.ResourceNotFoundException;
import com.ridelink.fare_payment_service.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ExternalServiceClient externalServiceClient;
    private final FareCalculationService fareCalculationService;

    public PaymentService(
            PaymentRepository paymentRepository,
            ExternalServiceClient externalServiceClient,
            FareCalculationService fareCalculationService) {

        this.paymentRepository = paymentRepository;
        this.externalServiceClient = externalServiceClient;
        this.fareCalculationService = fareCalculationService;
    }

    public PaymentResponse createPayment(PaymentRequest request) {

        // 1. Get completed ride details from Ride Management Service
        RideResponse ride =
                externalServiceClient.getRide(request.getRideId());

        if (!"COMPLETED".equalsIgnoreCase(ride.getStatus())) {
            throw new IllegalArgumentException(
                    "Payment can only be made for a completed ride"
            );
        }

        // 2. Get vehicle details from Driver & Vehicle Service
        VehicleResponse vehicle =
                externalServiceClient.getVehicle(ride.getVehicleId());

        // 3. Calculate the final fare using actual ride distance
        FareEstimateResponse fare =
                fareCalculationService.calculateFare(
                        vehicle.getVehicleType(),
                        ride.getDistanceKm()
                );

        // 4. Create payment record
        Payment payment = new Payment();

        payment.setRideId(ride.getRideId());
        payment.setPassengerId(ride.getPassengerId());
        payment.setVehicleId(ride.getVehicleId());
        payment.setVehicleType(vehicle.getVehicleType());

        payment.setDistanceKm(ride.getDistanceKm());
        payment.setBaseFare(fare.getBaseFare());
        payment.setPricePerKm(fare.getPricePerKm());
        payment.setDistanceCharge(fare.getDistanceCharge());
        payment.setDiscount(fare.getDiscount());
        payment.setTotalFare(fare.getTotalFare());

        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PAID);

        payment.setTransactionReference(
                "TXN-" + UUID.randomUUID()
        );

        payment.setPaymentDate(LocalDateTime.now());

        // 5. Save payment in Fare & Payment database
        Payment savedPayment =
                paymentRepository.save(payment);

        // 6. Return API response
        return new PaymentResponse(
                savedPayment.getId(),
                savedPayment.getRideId(),
                savedPayment.getTotalFare(),
                savedPayment.getPaymentMethod(),
                savedPayment.getPaymentStatus(),
                savedPayment.getTransactionReference(),
                savedPayment.getPaymentDate()
        );
    }

    public Payment getPayment(Long paymentId) {

        return paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + paymentId
                        )
                );
    }
}
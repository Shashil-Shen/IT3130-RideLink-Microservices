package com.ridelink.fare_payment_service.service;

import com.ridelink.fare_payment_service.dto.response.*;
import com.ridelink.fare_payment_service.entity.Payment;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {

    private final PaymentService paymentService;
    private final ExternalServiceClient externalServiceClient;

    public ReceiptService(
            PaymentService paymentService,
            ExternalServiceClient externalServiceClient) {

        this.paymentService = paymentService;
        this.externalServiceClient = externalServiceClient;
    }

    public ReceiptResponse generateReceipt(Long paymentId) {

        Payment payment = paymentService.getPayment(paymentId);

        RideResponse ride =
                externalServiceClient.getRide(
                        payment.getRideId()
                );

        PassengerResponse passenger =
                externalServiceClient.getPassenger(
                        payment.getPassengerId()
                );

        VehicleResponse vehicle =
                externalServiceClient.getVehicle(
                        payment.getVehicleId()
                );

        return new ReceiptResponse(
                payment.getId(),
                payment.getRideId(),

                passenger.getName(),

                vehicle.getVehicleNumber(),
                vehicle.getVehicleType(),
                vehicle.getOwnerName(),

                ride.getJourneyStart(),
                ride.getJourneyEnd(),

                payment.getDistanceKm(),

                payment.getBaseFare(),
                payment.getPricePerKm(),
                payment.getDistanceCharge(),
                payment.getDiscount(),
                payment.getTotalFare(),

                payment.getPaymentStatus(),
                payment.getTransactionReference(),
                payment.getPaymentDate()
        );
    }
}
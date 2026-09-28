package com.ridelink.fare_payment_service.dto.response;

import com.ridelink.fare_payment_service.enums.PaymentStatus;
import com.ridelink.fare_payment_service.enums.VehicleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReceiptResponse {

    private Long paymentId;
    private Long rideId;

    private String passengerName;

    private String vehicleNumber;
    private VehicleType vehicleType;
    private String vehicleOwnerName;

    private String journeyStart;
    private String journeyEnd;
    private BigDecimal distanceKm;

    private BigDecimal baseFare;
    private BigDecimal pricePerKm;
    private BigDecimal distanceCharge;
    private BigDecimal discount;
    private BigDecimal totalFare;

    private PaymentStatus paymentStatus;
    private String transactionReference;
    private LocalDateTime paymentDate;

    public ReceiptResponse(
            Long paymentId,
            Long rideId,
            String passengerName,
            String vehicleNumber,
            VehicleType vehicleType,
            String vehicleOwnerName,
            String journeyStart,
            String journeyEnd,
            BigDecimal distanceKm,
            BigDecimal baseFare,
            BigDecimal pricePerKm,
            BigDecimal distanceCharge,
            BigDecimal discount,
            BigDecimal totalFare,
            PaymentStatus paymentStatus,
            String transactionReference,
            LocalDateTime paymentDate) {

        this.paymentId = paymentId;
        this.rideId = rideId;
        this.passengerName = passengerName;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.vehicleOwnerName = vehicleOwnerName;
        this.journeyStart = journeyStart;
        this.journeyEnd = journeyEnd;
        this.distanceKm = distanceKm;
        this.baseFare = baseFare;
        this.pricePerKm = pricePerKm;
        this.distanceCharge = distanceCharge;
        this.discount = discount;
        this.totalFare = totalFare;
        this.paymentStatus = paymentStatus;
        this.transactionReference = transactionReference;
        this.paymentDate = paymentDate;
    }

    // Generate getters
}
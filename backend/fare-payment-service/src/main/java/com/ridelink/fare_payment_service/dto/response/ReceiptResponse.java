package com.ridelink.fare_payment_service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.ridelink.fare_payment_service.enums.PaymentStatus;
import com.ridelink.fare_payment_service.enums.VehicleType;

public class ReceiptResponse{

    private Long paymentId;
    private UUID rideId;

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
            UUID rideId,
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

    public Long getPaymentId() { return paymentId; }
    public UUID getRideId() { return rideId; }
    public String getPassengerName() { return passengerName; }
    public String getVehicleNumber() { return vehicleNumber; }
    public VehicleType getVehicleType() { return vehicleType; }
    public String getVehicleOwnerName() { return vehicleOwnerName; }
    public String getJourneyStart() { return journeyStart; }
    public String getJourneyEnd() { return journeyEnd; }
    public BigDecimal getDistanceKm() { return distanceKm; }
    public BigDecimal getBaseFare() { return baseFare; }
    public BigDecimal getPricePerKm() { return pricePerKm; }
    public BigDecimal getDistanceCharge() { return distanceCharge; }
    public BigDecimal getDiscount() { return discount; }
    public BigDecimal getTotalFare() { return totalFare; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public String getTransactionReference() { return transactionReference; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
}

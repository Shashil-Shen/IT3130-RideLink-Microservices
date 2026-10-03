package com.ridelink.fare_payment_service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ridelink.fare_payment_service.enums.PaymentStatus;

public class PaymentResponse {

    private Long paymentId;
    private Long rideId;
    private BigDecimal totalFare;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionReference;
    private LocalDateTime paymentDate;

    public PaymentResponse(
            Long paymentId,
            Long rideId,
            BigDecimal totalFare,
            String paymentMethod,
            PaymentStatus paymentStatus,
            String transactionReference,
            LocalDateTime paymentDate) {

        this.paymentId = paymentId;
        this.rideId = rideId;
        this.totalFare = totalFare;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionReference = transactionReference;
        this.paymentDate = paymentDate;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Long getRideId() {
        return rideId;
    }

    public BigDecimal getTotalFare() {
        return totalFare;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
}
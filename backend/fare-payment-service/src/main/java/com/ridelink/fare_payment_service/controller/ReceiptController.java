package com.ridelink.fare_payment_service.controller;

import com.ridelink.fare_payment_service.dto.response.ReceiptResponse;
import com.ridelink.fare_payment_service.service.ReceiptService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(
            ReceiptService receiptService) {

        this.receiptService = receiptService;
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ReceiptResponse> getReceipt(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                receiptService.generateReceipt(paymentId)
        );
    }
}

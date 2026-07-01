package com.smarterp.controllers;

import com.smarterp.dto.request.PaymentReceiptVoucherRequestDTO;
import com.smarterp.dto.response.VoucherResponseDTO;
import com.smarterp.services.PaymentReceiptVoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
@Tag(name = "Payment & Receipt Vouchers", description = "Payment and Receipt Voucher APIs")
public class PaymentReceiptVoucherController {

    private final PaymentReceiptVoucherService paymentReceiptVoucherService;

    @PostMapping("/payment")
    @Operation(summary = "Create payment voucher — pay supplier, reduce outstanding")
    public ResponseEntity<VoucherResponseDTO> createPaymentVoucher(
            @Valid @RequestBody PaymentReceiptVoucherRequestDTO request) {
        return ResponseEntity.ok(paymentReceiptVoucherService.createPaymentVoucher(request));
    }

    @PostMapping("/receipt")
    @Operation(summary = "Create receipt voucher — receive from customer, reduce outstanding")
    public ResponseEntity<VoucherResponseDTO> createReceiptVoucher(
            @Valid @RequestBody PaymentReceiptVoucherRequestDTO request) {
        return ResponseEntity.ok(paymentReceiptVoucherService.createReceiptVoucher(request));
    }

    @GetMapping("/payment/company/{companyId}")
    @Operation(summary = "Get all payment vouchers")
    public ResponseEntity<List<VoucherResponseDTO>> getPaymentVouchers(@PathVariable Long companyId) {
        return ResponseEntity.ok(paymentReceiptVoucherService.getPaymentVouchers(companyId));
    }

    @GetMapping("/receipt/company/{companyId}")
    @Operation(summary = "Get all receipt vouchers")
    public ResponseEntity<List<VoucherResponseDTO>> getReceiptVouchers(@PathVariable Long companyId) {
        return ResponseEntity.ok(paymentReceiptVoucherService.getReceiptVouchers(companyId));
    }
}
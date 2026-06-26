package com.smarterp.controllers;

import com.smarterp.dto.request.PurchaseVoucherRequestDTO;
import com.smarterp.dto.response.VoucherResponseDTO;
import com.smarterp.services.PurchaseVoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers/purchase")
@RequiredArgsConstructor
@Tag(name = "Purchase Voucher", description = "Purchase Voucher APIs")
public class PurchaseVoucherController {

    private final PurchaseVoucherService purchaseVoucherService;

    @PostMapping
    @Operation(summary = "Create purchase voucher — stock increases, supplier balance updates")
    public ResponseEntity<VoucherResponseDTO> createPurchaseVoucher(
            @Valid @RequestBody PurchaseVoucherRequestDTO request) {
        return ResponseEntity.ok(purchaseVoucherService.createPurchaseVoucher(request));
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get all purchase vouchers for a company")
    public ResponseEntity<List<VoucherResponseDTO>> getPurchaseVouchers(@PathVariable Long companyId) {
        return ResponseEntity.ok(purchaseVoucherService.getPurchaseVouchers(companyId));
    }
}
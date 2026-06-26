package com.smarterp.controllers;

import com.smarterp.dto.request.PurchaseVoucherRequestDTO;
import com.smarterp.dto.response.VoucherResponseDTO;
import com.smarterp.services.InvoicePdfService;
import com.smarterp.services.SalesVoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers/sales")
@RequiredArgsConstructor
@Tag(name = "Sales Voucher", description = "Sales Voucher APIs")
public class SalesVoucherController {

    private final SalesVoucherService salesVoucherService;
    private final InvoicePdfService invoicePdfService;

    @PostMapping
    @Operation(summary = "Create sales voucher — stock decreases, customer balance updates")
    public ResponseEntity<VoucherResponseDTO> createSalesVoucher(
            @Valid @RequestBody PurchaseVoucherRequestDTO request) {
        return ResponseEntity.ok(salesVoucherService.createSalesVoucher(request));
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get all sales vouchers")
    public ResponseEntity<List<VoucherResponseDTO>> getSalesVouchers(@PathVariable Long companyId) {
        return ResponseEntity.ok(salesVoucherService.getSalesVouchers(companyId));
    }

    @GetMapping("/{voucherId}/pdf")
    @Operation(summary = "Download PDF invoice for a sales voucher")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long voucherId) {
        byte[] pdf = invoicePdfService.generateInvoicePdf(voucherId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + voucherId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
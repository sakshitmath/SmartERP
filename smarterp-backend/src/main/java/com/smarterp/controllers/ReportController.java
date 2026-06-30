package com.smarterp.controllers;

import com.smarterp.dto.response.*;
import com.smarterp.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Financial Reports APIs")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/trial-balance/{companyId}")
    @Operation(summary = "Get Trial Balance")
    public ResponseEntity<TrialBalanceDTO> getTrialBalance(@PathVariable Long companyId) {
        return ResponseEntity.ok(reportService.getTrialBalance(companyId));
    }

    @GetMapping("/stock-summary/{companyId}")
    @Operation(summary = "Get Stock Summary")
    public ResponseEntity<StockSummaryDTO> getStockSummary(@PathVariable Long companyId) {
        return ResponseEntity.ok(reportService.getStockSummary(companyId));
    }

    @GetMapping("/balance-sheet/{companyId}")
    @Operation(summary = "Get Balance Sheet")
    public ResponseEntity<BalanceSheetDTO> getBalanceSheet(@PathVariable Long companyId) {
        return ResponseEntity.ok(reportService.getBalanceSheet(companyId));
    }

    @GetMapping("/profit-loss/{companyId}")
    @Operation(summary = "Get Profit & Loss Statement")
    public ResponseEntity<ProfitLossDTO> getProfitLoss(@PathVariable Long companyId) {
        return ResponseEntity.ok(reportService.getProfitLoss(companyId));
    }
}
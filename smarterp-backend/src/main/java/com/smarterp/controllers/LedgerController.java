package com.smarterp.controllers;

import com.smarterp.dto.request.LedgerRequestDTO;
import com.smarterp.dto.response.LedgerResponseDTO;
import com.smarterp.entities.Ledger;
import com.smarterp.services.LedgerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ledgers")
@RequiredArgsConstructor
@Tag(name = "Ledger", description = "Ledger Management APIs")
public class LedgerController {

    private final LedgerService ledgerService;

    @PostMapping
    @Operation(summary = "Create a new ledger")
    public ResponseEntity<LedgerResponseDTO> createLedger(@Valid @RequestBody LedgerRequestDTO request) {
        return ResponseEntity.ok(ledgerService.createLedger(request));
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get all ledgers for a company")
    public ResponseEntity<List<LedgerResponseDTO>> getAllLedgers(@PathVariable Long companyId) {
        return ResponseEntity.ok(ledgerService.getAllLedgers(companyId));
    }

    @GetMapping("/company/{companyId}/type/{type}")
    @Operation(summary = "Get ledgers by type")
    public ResponseEntity<List<LedgerResponseDTO>> getLedgersByType(@PathVariable Long companyId,
                                                                    @PathVariable Ledger.LedgerType type) {
        return ResponseEntity.ok(ledgerService.getLedgersByType(companyId, type));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ledger by ID")
    public ResponseEntity<LedgerResponseDTO> getLedgerById(@PathVariable Long id) {
        return ResponseEntity.ok(ledgerService.getLedgerById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ledger")
    public ResponseEntity<LedgerResponseDTO> updateLedger(@PathVariable Long id,
                                                          @Valid @RequestBody LedgerRequestDTO request) {
        return ResponseEntity.ok(ledgerService.updateLedger(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete ledger")
    public ResponseEntity<String> deleteLedger(@PathVariable Long id) {
        ledgerService.deleteLedger(id);
        return ResponseEntity.ok("Ledger deleted successfully");
    }
}
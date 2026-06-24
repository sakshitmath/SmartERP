package com.smarterp.controllers;

import com.smarterp.dto.request.StockItemRequestDTO;
import com.smarterp.dto.response.StockItemResponseDTO;
import com.smarterp.services.StockItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-items")
@RequiredArgsConstructor
@Tag(name = "Stock Items", description = "Inventory Management APIs")
public class StockItemController {

    private final StockItemService stockItemService;

    @PostMapping
    @Operation(summary = "Create a new stock item")
    public ResponseEntity<StockItemResponseDTO> createStockItem(@Valid @RequestBody StockItemRequestDTO request) {
        return ResponseEntity.ok(stockItemService.createStockItem(request));
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get all stock items for a company")
    public ResponseEntity<List<StockItemResponseDTO>> getAllStockItems(@PathVariable Long companyId) {
        return ResponseEntity.ok(stockItemService.getAllStockItems(companyId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get stock item by ID")
    public ResponseEntity<StockItemResponseDTO> getStockItemById(@PathVariable Long id) {
        return ResponseEntity.ok(stockItemService.getStockItemById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update stock item")
    public ResponseEntity<StockItemResponseDTO> updateStockItem(@PathVariable Long id,
                                                                @Valid @RequestBody StockItemRequestDTO request) {
        return ResponseEntity.ok(stockItemService.updateStockItem(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete stock item")
    public ResponseEntity<String> deleteStockItem(@PathVariable Long id) {
        stockItemService.deleteStockItem(id);
        return ResponseEntity.ok("Stock item deleted successfully");
    }
}
package com.smarterp.services;

import com.smarterp.dto.request.StockItemRequestDTO;
import com.smarterp.dto.response.StockItemResponseDTO;
import com.smarterp.entities.Company;
import com.smarterp.entities.StockItem;
import com.smarterp.repositories.CompanyRepository;
import com.smarterp.repositories.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockItemService {

    private final StockItemRepository stockItemRepository;
    private final CompanyRepository companyRepository;

    private StockItemResponseDTO toDTO(StockItem item) {
        return StockItemResponseDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .sku(item.getSku())
                .hsnCode(item.getHsnCode())
                .purchasePrice(item.getPurchasePrice())
                .sellingPrice(item.getSellingPrice())
                .quantity(item.getQuantity())
                .reorderLevel(item.getReorderLevel())
                .gstRate(item.getGstRate())
                .unit(item.getUnit())
                .stockGroup(item.getStockGroup())
                .build();
    }

    public StockItemResponseDTO createStockItem(StockItemRequestDTO request) {
        Company company = companyRepository.findByIdAndIsDeletedFalse(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (request.getSku() != null && stockItemRepository.existsBySkuAndIsDeletedFalse(request.getSku())) {
            throw new RuntimeException("SKU already exists");
        }

        StockItem item = StockItem.builder()
                .name(request.getName())
                .sku(request.getSku())
                .hsnCode(request.getHsnCode())
                .purchasePrice(request.getPurchasePrice())
                .sellingPrice(request.getSellingPrice())
                .quantity(request.getQuantity())
                .reorderLevel(request.getReorderLevel())
                .gstRate(request.getGstRate())
                .unit(request.getUnit())
                .stockGroup(request.getStockGroup())
                .company(company)
                .build();

        return toDTO(stockItemRepository.save(item));
    }

    public List<StockItemResponseDTO> getAllStockItems(Long companyId) {
        return stockItemRepository.findByCompanyIdAndIsDeletedFalse(companyId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public StockItemResponseDTO getStockItemById(Long id) {
        return toDTO(stockItemRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Stock item not found")));
    }

    public StockItemResponseDTO updateStockItem(Long id, StockItemRequestDTO request) {
        StockItem item = stockItemRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Stock item not found"));

        item.setName(request.getName());
        item.setHsnCode(request.getHsnCode());
        item.setPurchasePrice(request.getPurchasePrice());
        item.setSellingPrice(request.getSellingPrice());
        item.setReorderLevel(request.getReorderLevel());
        item.setGstRate(request.getGstRate());
        item.setUnit(request.getUnit());
        item.setStockGroup(request.getStockGroup());

        return toDTO(stockItemRepository.save(item));
    }

    public void deleteStockItem(Long id) {
        StockItem item = stockItemRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Stock item not found"));
        item.setDeleted(true);
        stockItemRepository.save(item);
    }
}
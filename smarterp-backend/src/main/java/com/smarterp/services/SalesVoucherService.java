package com.smarterp.services;

import com.smarterp.dto.request.PurchaseVoucherRequestDTO;
import com.smarterp.dto.response.VoucherResponseDTO;
import com.smarterp.entities.*;
import com.smarterp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.smarterp.entities.LedgerGroup;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesVoucherService {

    private final VoucherRepository voucherRepository;
    private final StockItemRepository stockItemRepository;
    private final LedgerRepository ledgerRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public VoucherResponseDTO createSalesVoucher(PurchaseVoucherRequestDTO request) {

        Company company = companyRepository.findByIdAndIsDeletedFalse(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Ledger customerLedger = ledgerRepository.findByIdAndIsDeletedFalse(request.getSupplierLedgerId())
                .orElseThrow(() -> new RuntimeException("Customer ledger not found"));

        // Generate voucher number
        long count = voucherRepository.countByCompanyIdAndVoucherType(
                request.getCompanyId(), Voucher.VoucherType.SALES);
        String voucherNumber = "SAL-" + request.getCompanyId() + "-" + String.format("%04d", count + 1);

        List<VoucherItem> voucherItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalGst = BigDecimal.ZERO;

        for (PurchaseVoucherRequestDTO.PurchaseItemDTO itemDTO : request.getItems()) {
            StockItem stockItem = stockItemRepository.findByIdAndIsDeletedFalse(itemDTO.getStockItemId())
                    .orElseThrow(() -> new RuntimeException("Stock item not found"));

            // Check stock availability
            if (stockItem.getQuantity().compareTo(itemDTO.getQuantity()) < 0) {
                throw new RuntimeException("Insufficient stock for: " + stockItem.getName()
                        + ". Available: " + stockItem.getQuantity());
            }

            BigDecimal amount = itemDTO.getRate().multiply(itemDTO.getQuantity());
            BigDecimal gstAmount = amount.multiply(stockItem.getGstRate())
                    .divide(BigDecimal.valueOf(100));
            BigDecimal itemTotal = amount.add(gstAmount);

            // Decrease stock quantity
            stockItem.setQuantity(stockItem.getQuantity().subtract(itemDTO.getQuantity()));
            stockItemRepository.save(stockItem);

            VoucherItem voucherItem = VoucherItem.builder()
                    .stockItem(stockItem)
                    .quantity(itemDTO.getQuantity())
                    .rate(itemDTO.getRate())
                    .amount(amount)
                    .gstRate(stockItem.getGstRate())
                    .gstAmount(gstAmount)
                    .totalAmount(itemTotal)
                    .build();

            voucherItems.add(voucherItem);
            totalAmount = totalAmount.add(amount);
            totalGst = totalGst.add(gstAmount);
        }

        BigDecimal grandTotal = totalAmount.add(totalGst);

        // Update customer ledger balance
        customerLedger.setCurrentBalance(customerLedger.getCurrentBalance().add(grandTotal));
        ledgerRepository.save(customerLedger);
        // Double-entry: credit Sales Account ledger (income goes up)
        // Double-entry: credit Sales Account
        ledgerRepository.findByCompanyIdAndIsDeletedFalse(request.getCompanyId())
                .stream()
                .filter(l -> l.getName().equalsIgnoreCase("Sales Account"))
                .findFirst()
                .ifPresent(salesLedger -> {
                    salesLedger.setCurrentBalance(salesLedger.getCurrentBalance().add(grandTotal));
                    ledgerRepository.save(salesLedger);
                });

        Voucher voucher = Voucher.builder()
                .voucherType(Voucher.VoucherType.SALES)
                .voucherNumber(voucherNumber)
                .voucherDate(request.getVoucherDate())
                .company(company)
                .ledger(customerLedger)
                .totalAmount(totalAmount)
                .totalGst(totalGst)
                .grandTotal(grandTotal)
                .notes(request.getNotes())
                .build();

        Voucher saved = voucherRepository.save(voucher);
        voucherItems.forEach(item -> item.setVoucher(saved));
        saved.setItems(voucherItems);
        voucherRepository.save(saved);

        return toDTO(saved);
    }

    @Transactional
    public List<VoucherResponseDTO> getSalesVouchers(Long companyId) {
        return voucherRepository
                .findByCompanyIdAndVoucherTypeAndIsDeletedFalse(companyId, Voucher.VoucherType.SALES)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private VoucherResponseDTO toDTO(Voucher voucher) {
        List<VoucherResponseDTO.VoucherItemDTO> itemDTOs = voucher.getItems() == null ? List.of() :
                voucher.getItems().stream().map(item ->
                        VoucherResponseDTO.VoucherItemDTO.builder()
                                .stockItemId(item.getStockItem().getId())
                                .stockItemName(item.getStockItem().getName())
                                .quantity(item.getQuantity())
                                .rate(item.getRate())
                                .amount(item.getAmount())
                                .gstRate(item.getGstRate())
                                .gstAmount(item.getGstAmount())
                                .totalAmount(item.getTotalAmount())
                                .build()
                ).collect(Collectors.toList());

        return VoucherResponseDTO.builder()
                .id(voucher.getId())
                .voucherNumber(voucher.getVoucherNumber())
                .voucherType(voucher.getVoucherType().name())
                .voucherDate(voucher.getVoucherDate())
                .ledgerName(voucher.getLedger().getName())
                .totalAmount(voucher.getTotalAmount())
                .totalGst(voucher.getTotalGst())
                .grandTotal(voucher.getGrandTotal())
                .notes(voucher.getNotes())
                .items(itemDTOs)
                .build();
    }
}
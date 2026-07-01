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
public class PurchaseVoucherService {

    private final VoucherRepository voucherRepository;
    private final StockItemRepository stockItemRepository;
    private final LedgerRepository ledgerRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public VoucherResponseDTO createPurchaseVoucher(PurchaseVoucherRequestDTO request) {

        Company company = companyRepository.findByIdAndIsDeletedFalse(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Ledger supplierLedger = ledgerRepository.findByIdAndIsDeletedFalse(request.getSupplierLedgerId())
                .orElseThrow(() -> new RuntimeException("Supplier ledger not found"));

        // Generate voucher number
        long count = voucherRepository.countByCompanyIdAndVoucherType(
                request.getCompanyId(), Voucher.VoucherType.PURCHASE);
        String voucherNumber = "PUR-" + request.getCompanyId() + "-" + String.format("%04d", count + 1);

        // Build items + calculate totals
        List<VoucherItem> voucherItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalGst = BigDecimal.ZERO;

        for (PurchaseVoucherRequestDTO.PurchaseItemDTO itemDTO : request.getItems()) {
            StockItem stockItem = stockItemRepository.findByIdAndIsDeletedFalse(itemDTO.getStockItemId())
                    .orElseThrow(() -> new RuntimeException("Stock item not found"));

            BigDecimal amount = itemDTO.getRate().multiply(itemDTO.getQuantity());
            BigDecimal gstAmount = amount.multiply(stockItem.getGstRate())
                    .divide(BigDecimal.valueOf(100));
            BigDecimal itemTotal = amount.add(gstAmount);

            // Increase stock quantity
            stockItem.setQuantity(stockItem.getQuantity().add(itemDTO.getQuantity()));
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

        // Update supplier ledger balance
        supplierLedger.setCurrentBalance(supplierLedger.getCurrentBalance().add(grandTotal));
        ledgerRepository.save(supplierLedger);
        // Double-entry: debit Purchase Account ledger (expense goes up)
        List<Ledger> purchaseAccounts = ledgerRepository.findByCompanyIdAndGroupNature(
                request.getCompanyId(), LedgerGroup.GroupNature.EXPENSES);
        if (!purchaseAccounts.isEmpty()) {
            Ledger purchaseAccount = purchaseAccounts.get(0);
            purchaseAccount.setCurrentBalance(purchaseAccount.getCurrentBalance().add(grandTotal));
            ledgerRepository.save(purchaseAccount);
        }

        // Save voucher
        Voucher voucher = Voucher.builder()
                .voucherType(Voucher.VoucherType.PURCHASE)
                .voucherNumber(voucherNumber)
                .voucherDate(request.getVoucherDate())
                .company(company)
                .ledger(supplierLedger)
                .totalAmount(totalAmount)
                .totalGst(totalGst)
                .grandTotal(grandTotal)
                .notes(request.getNotes())
                .build();

        Voucher saved = voucherRepository.save(voucher);

        // Set voucher reference in items
        voucherItems.forEach(item -> item.setVoucher(saved));
        saved.setItems(voucherItems);
        voucherRepository.save(saved);

        return toDTO(saved);
    }


    @Transactional
    public List<VoucherResponseDTO> getPurchaseVouchers(Long companyId) {
        return voucherRepository
                .findByCompanyIdAndVoucherTypeAndIsDeletedFalse(companyId, Voucher.VoucherType.PURCHASE)
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
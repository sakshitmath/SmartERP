package com.smarterp.services;

import com.smarterp.dto.request.PaymentReceiptVoucherRequestDTO;
import com.smarterp.dto.response.VoucherResponseDTO;
import com.smarterp.entities.*;
import com.smarterp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentReceiptVoucherService {

    private final VoucherRepository voucherRepository;
    private final LedgerRepository ledgerRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public VoucherResponseDTO createPaymentVoucher(PaymentReceiptVoucherRequestDTO request) {
        Company company = companyRepository.findByIdAndIsDeletedFalse(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Ledger ledger = ledgerRepository.findByIdAndIsDeletedFalse(request.getLedgerId())
                .orElseThrow(() -> new RuntimeException("Ledger not found"));

        // Reduce supplier outstanding balance
        ledger.setCurrentBalance(ledger.getCurrentBalance().subtract(request.getAmount()));
        ledgerRepository.save(ledger);

        long count = voucherRepository.countByCompanyIdAndVoucherType(
                request.getCompanyId(), Voucher.VoucherType.PAYMENT);
        String voucherNumber = "PAY-" + request.getCompanyId() + "-" + String.format("%04d", count + 1);

        Voucher voucher = Voucher.builder()
                .voucherType(Voucher.VoucherType.PAYMENT)
                .voucherNumber(voucherNumber)
                .voucherDate(request.getVoucherDate())
                .company(company)
                .ledger(ledger)
                .totalAmount(request.getAmount())
                .totalGst(java.math.BigDecimal.ZERO)
                .grandTotal(request.getAmount())
                .notes(request.getNotes())
                .build();

        Voucher saved = voucherRepository.save(voucher);
        return toDTO(saved);
    }

    @Transactional
    public VoucherResponseDTO createReceiptVoucher(PaymentReceiptVoucherRequestDTO request) {
        Company company = companyRepository.findByIdAndIsDeletedFalse(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Ledger ledger = ledgerRepository.findByIdAndIsDeletedFalse(request.getLedgerId())
                .orElseThrow(() -> new RuntimeException("Ledger not found"));

        // Reduce customer outstanding balance
        ledger.setCurrentBalance(ledger.getCurrentBalance().subtract(request.getAmount()));
        ledgerRepository.save(ledger);

        long count = voucherRepository.countByCompanyIdAndVoucherType(
                request.getCompanyId(), Voucher.VoucherType.RECEIPT);
        String voucherNumber = "REC-" + request.getCompanyId() + "-" + String.format("%04d", count + 1);

        Voucher voucher = Voucher.builder()
                .voucherType(Voucher.VoucherType.RECEIPT)
                .voucherNumber(voucherNumber)
                .voucherDate(request.getVoucherDate())
                .company(company)
                .ledger(ledger)
                .totalAmount(request.getAmount())
                .totalGst(java.math.BigDecimal.ZERO)
                .grandTotal(request.getAmount())
                .notes(request.getNotes())
                .build();

        Voucher saved = voucherRepository.save(voucher);
        return toDTO(saved);
    }

    @Transactional
    public List<VoucherResponseDTO> getPaymentVouchers(Long companyId) {
        return voucherRepository
                .findByCompanyIdAndVoucherTypeAndIsDeletedFalse(companyId, Voucher.VoucherType.PAYMENT)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<VoucherResponseDTO> getReceiptVouchers(Long companyId) {
        return voucherRepository
                .findByCompanyIdAndVoucherTypeAndIsDeletedFalse(companyId, Voucher.VoucherType.RECEIPT)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private VoucherResponseDTO toDTO(Voucher voucher) {
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
                .build();
    }
}
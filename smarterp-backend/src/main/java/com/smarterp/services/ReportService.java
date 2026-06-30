package com.smarterp.services;

import com.smarterp.dto.response.*;
import com.smarterp.entities.Ledger;
import com.smarterp.entities.LedgerGroup;
import com.smarterp.entities.StockItem;
import com.smarterp.repositories.LedgerGroupRepository;
import com.smarterp.repositories.LedgerRepository;
import com.smarterp.repositories.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final LedgerRepository ledgerRepository;
    private final LedgerGroupRepository ledgerGroupRepository;
    private final StockItemRepository stockItemRepository;

    @Transactional
    public TrialBalanceDTO getTrialBalance(Long companyId) {
        List<Ledger> ledgers = ledgerRepository.findByCompanyIdAndIsDeletedFalse(companyId);

        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        List<TrialBalanceDTO.TrialBalanceEntryDTO> entries = ledgers.stream().map(ledger -> {
            String balanceType = ledger.getCurrentBalance().compareTo(BigDecimal.ZERO) >= 0 ? "DR" : "CR";
            return TrialBalanceDTO.TrialBalanceEntryDTO.builder()
                    .ledgerName(ledger.getName())
                    .groupName(ledger.getGroup().getName())
                    .nature(ledger.getType().name())
                    .balance(ledger.getCurrentBalance().abs())
                    .balanceType(balanceType)
                    .build();
        }).collect(Collectors.toList());

        for (TrialBalanceDTO.TrialBalanceEntryDTO entry : entries) {
            if (entry.getBalanceType().equals("DR")) {
                totalDebit = totalDebit.add(entry.getBalance());
            } else {
                totalCredit = totalCredit.add(entry.getBalance());
            }
        }

        return TrialBalanceDTO.builder()
                .entries(entries)
                .totalDebit(totalDebit)
                .totalCredit(totalCredit)
                .build();
    }

    @Transactional
    public StockSummaryDTO getStockSummary(Long companyId) {
        List<StockItem> items = stockItemRepository.findByCompanyIdAndIsDeletedFalse(companyId);

        BigDecimal totalValue = BigDecimal.ZERO;

        List<StockSummaryDTO.StockSummaryEntryDTO> entries = items.stream().map(item -> {
            BigDecimal itemValue = item.getPurchasePrice().multiply(item.getQuantity());
            String status = item.getQuantity().compareTo(item.getReorderLevel()) <= 0
                    ? "LOW STOCK" : "OK";
            return StockSummaryDTO.StockSummaryEntryDTO.builder()
                    .itemName(item.getName())
                    .sku(item.getSku())
                    .unit(item.getUnit())
                    .quantity(item.getQuantity())
                    .purchasePrice(item.getPurchasePrice())
                    .totalValue(itemValue)
                    .status(status)
                    .build();
        }).collect(Collectors.toList());

        for (StockSummaryDTO.StockSummaryEntryDTO entry : entries) {
            totalValue = totalValue.add(entry.getTotalValue());
        }

        return StockSummaryDTO.builder()
                .items(entries)
                .totalValue(totalValue)
                .build();
    }

    @Transactional
    public BalanceSheetDTO getBalanceSheet(Long companyId) {
        List<LedgerGroup> groups = ledgerGroupRepository.findByCompanyIdAndIsDeletedFalse(companyId);

        List<BalanceSheetDTO.BalanceSheetEntryDTO> assets = ledgerRepository
                .findByCompanyIdAndIsDeletedFalse(companyId).stream()
                .filter(l -> l.getGroup().getNature() == LedgerGroup.GroupNature.ASSETS)
                .map(l -> BalanceSheetDTO.BalanceSheetEntryDTO.builder()
                        .ledgerName(l.getName())
                        .groupName(l.getGroup().getName())
                        .balance(l.getCurrentBalance())
                        .build())
                .collect(Collectors.toList());

        List<BalanceSheetDTO.BalanceSheetEntryDTO> liabilities = ledgerRepository
                .findByCompanyIdAndIsDeletedFalse(companyId).stream()
                .filter(l -> l.getGroup().getNature() == LedgerGroup.GroupNature.LIABILITIES)
                .map(l -> BalanceSheetDTO.BalanceSheetEntryDTO.builder()
                        .ledgerName(l.getName())
                        .groupName(l.getGroup().getName())
                        .balance(l.getCurrentBalance())
                        .build())
                .collect(Collectors.toList());

        BigDecimal totalAssets = assets.stream()
                .map(BalanceSheetDTO.BalanceSheetEntryDTO::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalLiabilities = liabilities.stream()
                .map(BalanceSheetDTO.BalanceSheetEntryDTO::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return BalanceSheetDTO.builder()
                .assets(assets)
                .liabilities(liabilities)
                .totalAssets(totalAssets)
                .totalLiabilities(totalLiabilities)
                .build();
    }

    @Transactional
    public ProfitLossDTO getProfitLoss(Long companyId) {
        List<ProfitLossDTO.ProfitLossEntryDTO> incomes = ledgerRepository
                .findByCompanyIdAndIsDeletedFalse(companyId).stream()
                .filter(l -> l.getGroup().getNature() == LedgerGroup.GroupNature.INCOME)
                .map(l -> ProfitLossDTO.ProfitLossEntryDTO.builder()
                        .ledgerName(l.getName())
                        .groupName(l.getGroup().getName())
                        .balance(l.getCurrentBalance())
                        .build())
                .collect(Collectors.toList());

        List<ProfitLossDTO.ProfitLossEntryDTO> expenses = ledgerRepository
                .findByCompanyIdAndIsDeletedFalse(companyId).stream()
                .filter(l -> l.getGroup().getNature() == LedgerGroup.GroupNature.EXPENSES)
                .map(l -> ProfitLossDTO.ProfitLossEntryDTO.builder()
                        .ledgerName(l.getName())
                        .groupName(l.getGroup().getName())
                        .balance(l.getCurrentBalance())
                        .build())
                .collect(Collectors.toList());

        BigDecimal totalIncome = incomes.stream()
                .map(ProfitLossDTO.ProfitLossEntryDTO::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = expenses.stream()
                .map(ProfitLossDTO.ProfitLossEntryDTO::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netProfitOrLoss = totalIncome.subtract(totalExpense);
        String result = netProfitOrLoss.compareTo(BigDecimal.ZERO) >= 0 ? "NET PROFIT" : "NET LOSS";

        return ProfitLossDTO.builder()
                .incomes(incomes)
                .expenses(expenses)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netProfitOrLoss(netProfitOrLoss.abs())
                .result(result)
                .build();
    }
}
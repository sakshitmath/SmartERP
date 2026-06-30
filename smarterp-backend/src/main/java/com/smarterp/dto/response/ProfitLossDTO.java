package com.smarterp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProfitLossDTO {
    private List<ProfitLossEntryDTO> incomes;
    private List<ProfitLossEntryDTO> expenses;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netProfitOrLoss;
    private String result;

    @Data
    @Builder
    public static class ProfitLossEntryDTO {
        private String ledgerName;
        private String groupName;
        private BigDecimal balance;
    }
}
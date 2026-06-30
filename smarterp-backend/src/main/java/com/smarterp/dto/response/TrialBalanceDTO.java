package com.smarterp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class TrialBalanceDTO {
    private List<TrialBalanceEntryDTO> entries;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;

    @Data
    @Builder
    public static class TrialBalanceEntryDTO {
        private String ledgerName;
        private String groupName;
        private String nature;
        private BigDecimal balance;
        private String balanceType;
    }
}
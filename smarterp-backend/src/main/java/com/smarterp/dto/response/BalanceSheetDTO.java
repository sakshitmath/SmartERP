package com.smarterp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class BalanceSheetDTO {
    private List<BalanceSheetEntryDTO> assets;
    private List<BalanceSheetEntryDTO> liabilities;
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;

    @Data
    @Builder
    public static class BalanceSheetEntryDTO {
        private String ledgerName;
        private String groupName;
        private BigDecimal balance;
    }
}
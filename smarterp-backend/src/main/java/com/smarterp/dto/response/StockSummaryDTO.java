package com.smarterp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class StockSummaryDTO {
    private List<StockSummaryEntryDTO> items;
    private BigDecimal totalValue;

    @Data
    @Builder
    public static class StockSummaryEntryDTO {
        private String itemName;
        private String sku;
        private String unit;
        private BigDecimal quantity;
        private BigDecimal purchasePrice;
        private BigDecimal totalValue;
        private String status;
    }
}
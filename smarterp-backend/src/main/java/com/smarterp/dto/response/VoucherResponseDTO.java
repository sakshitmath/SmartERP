package com.smarterp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class VoucherResponseDTO {
    private Long id;
    private String voucherNumber;
    private String voucherType;
    private LocalDate voucherDate;
    private String ledgerName;
    private BigDecimal totalAmount;
    private BigDecimal totalGst;
    private BigDecimal grandTotal;
    private String notes;
    private List<VoucherItemDTO> items;

    @Data
    @Builder
    public static class VoucherItemDTO {
        private Long stockItemId;
        private String stockItemName;
        private BigDecimal quantity;
        private BigDecimal rate;
        private BigDecimal amount;
        private BigDecimal gstRate;
        private BigDecimal gstAmount;
        private BigDecimal totalAmount;
    }
}
package com.smarterp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseVoucherRequestDTO {

    @NotNull
    private Long companyId;

    @NotNull
    private Long supplierLedgerId;

    @NotNull
    private LocalDate voucherDate;

    private String notes;

    @NotNull
    private List<PurchaseItemDTO> items;

    @Data
    public static class PurchaseItemDTO {
        @NotNull
        private Long stockItemId;
        @NotNull
        private BigDecimal quantity;
        @NotNull
        private BigDecimal rate;
    }
}
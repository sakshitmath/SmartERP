package com.smarterp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentReceiptVoucherRequestDTO {

    @NotNull
    private Long companyId;

    @NotNull
    private Long ledgerId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate voucherDate;

    private String notes;
}
package com.smarterp.dto.request;

import com.smarterp.entities.Ledger;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class LedgerRequestDTO {

    @NotBlank(message = "Ledger name is required")
    private String name;

    @NotNull(message = "Ledger type is required")
    private Ledger.LedgerType type;

    @NotNull(message = "Group ID is required")
    private Long groupId;

    @NotNull(message = "Company ID is required")
    private Long companyId;

    private BigDecimal openingBalance = BigDecimal.ZERO;
    private String gstNumber;
    private String contactNumber;
    private String email;
    private String address;
}
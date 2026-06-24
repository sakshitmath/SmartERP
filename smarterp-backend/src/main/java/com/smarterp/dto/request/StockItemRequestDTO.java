package com.smarterp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class StockItemRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String sku;
    private String hsnCode;

    @NotNull(message = "Purchase price is required")
    private BigDecimal purchasePrice;

    @NotNull(message = "Selling price is required")
    private BigDecimal sellingPrice;

    private BigDecimal quantity = BigDecimal.ZERO;
    private BigDecimal reorderLevel = BigDecimal.ZERO;
    private BigDecimal gstRate = BigDecimal.ZERO;
    private String unit;
    private String stockGroup;

    @NotNull(message = "Company ID is required")
    private Long companyId;
}
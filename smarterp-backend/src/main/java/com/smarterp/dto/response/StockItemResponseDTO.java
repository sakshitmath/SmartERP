package com.smarterp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class StockItemResponseDTO {
    private Long id;
    private String name;
    private String sku;
    private String hsnCode;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private BigDecimal quantity;
    private BigDecimal reorderLevel;
    private BigDecimal gstRate;
    private String unit;
    private String stockGroup;
}
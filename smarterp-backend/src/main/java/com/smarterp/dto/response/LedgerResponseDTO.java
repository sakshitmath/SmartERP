package com.smarterp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class LedgerResponseDTO {
    private Long id;
    private String name;
    private String type;
    private String groupName;
    private BigDecimal openingBalance;
    private BigDecimal currentBalance;
    private String gstNumber;
    private String contactNumber;
    private String email;
    private String address;
}
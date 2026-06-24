package com.smarterp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class CompanyResponseDTO {
    private Long id;
    private String name;
    private String gstNumber;
    private String address;
    private String state;
    private String pinCode;
    private String contactNumber;
    private String email;
    private LocalDate financialYearStart;
    private LocalDate financialYearEnd;
}
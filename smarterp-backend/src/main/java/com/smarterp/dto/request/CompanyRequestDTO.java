package com.smarterp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CompanyRequestDTO {

    @NotBlank(message = "Company name is required")
    private String name;

    private String gstNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "State is required")
    private String state;

    private String pinCode;
    private String contactNumber;
    private String email;

    @NotNull(message = "Financial year start is required")
    private LocalDate financialYearStart;

    @NotNull(message = "Financial year end is required")
    private LocalDate financialYearEnd;
}
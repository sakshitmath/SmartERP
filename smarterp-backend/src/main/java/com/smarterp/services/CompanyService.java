package com.smarterp.services;

import com.smarterp.dto.request.CompanyRequestDTO;
import com.smarterp.dto.response.CompanyResponseDTO;
import com.smarterp.entities.Company;
import com.smarterp.entities.User;
import com.smarterp.repositories.CompanyRepository;
import com.smarterp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private CompanyResponseDTO toDTO(Company company) {
        return CompanyResponseDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .gstNumber(company.getGstNumber())
                .address(company.getAddress())
                .state(company.getState())
                .pinCode(company.getPinCode())
                .contactNumber(company.getContactNumber())
                .email(company.getEmail())
                .financialYearStart(company.getFinancialYearStart())
                .financialYearEnd(company.getFinancialYearEnd())
                .build();
    }

    public CompanyResponseDTO createCompany(CompanyRequestDTO request) {
        User user = getCurrentUser();

        if (companyRepository.countByUserIdAndIsDeletedFalse(user.getId()) >= 5) {
            throw new RuntimeException("Maximum 5 companies allowed per user");
        }

        Company company = Company.builder()
                .name(request.getName())
                .gstNumber(request.getGstNumber())
                .address(request.getAddress())
                .state(request.getState())
                .pinCode(request.getPinCode())
                .contactNumber(request.getContactNumber())
                .email(request.getEmail())
                .financialYearStart(request.getFinancialYearStart())
                .financialYearEnd(request.getFinancialYearEnd())
                .user(user)
                .build();

        return toDTO(companyRepository.save(company));
    }

    public List<CompanyResponseDTO> getAllCompanies() {
        User user = getCurrentUser();
        return companyRepository.findByUserIdAndIsDeletedFalse(user.getId())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CompanyResponseDTO getCompanyById(Long id) {
        Company company = companyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return toDTO(company);
    }

    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO request) {
        Company company = companyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setName(request.getName());
        company.setGstNumber(request.getGstNumber());
        company.setAddress(request.getAddress());
        company.setState(request.getState());
        company.setPinCode(request.getPinCode());
        company.setContactNumber(request.getContactNumber());
        company.setEmail(request.getEmail());
        company.setFinancialYearStart(request.getFinancialYearStart());
        company.setFinancialYearEnd(request.getFinancialYearEnd());

        return toDTO(companyRepository.save(company));
    }

    public void deleteCompany(Long id) {
        Company company = companyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setDeleted(true);
        companyRepository.save(company);
    }
}
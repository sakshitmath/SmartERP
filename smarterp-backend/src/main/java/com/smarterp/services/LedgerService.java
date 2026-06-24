package com.smarterp.services;

import com.smarterp.dto.request.LedgerRequestDTO;
import com.smarterp.dto.response.LedgerResponseDTO;
import com.smarterp.entities.Company;
import com.smarterp.entities.Ledger;
import com.smarterp.entities.LedgerGroup;
import com.smarterp.repositories.CompanyRepository;
import com.smarterp.repositories.LedgerGroupRepository;
import com.smarterp.repositories.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerRepository ledgerRepository;
    private final LedgerGroupRepository ledgerGroupRepository;
    private final CompanyRepository companyRepository;

    private LedgerResponseDTO toDTO(Ledger ledger) {
        return LedgerResponseDTO.builder()
                .id(ledger.getId())
                .name(ledger.getName())
                .type(ledger.getType().name())
                .groupName(ledger.getGroup().getName())
                .openingBalance(ledger.getOpeningBalance())
                .currentBalance(ledger.getCurrentBalance())
                .gstNumber(ledger.getGstNumber())
                .contactNumber(ledger.getContactNumber())
                .email(ledger.getEmail())
                .address(ledger.getAddress())
                .build();
    }

    public LedgerResponseDTO createLedger(LedgerRequestDTO request) {
        Company company = companyRepository.findByIdAndIsDeletedFalse(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        LedgerGroup group = ledgerGroupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Ledger group not found"));

        Ledger ledger = Ledger.builder()
                .name(request.getName())
                .type(request.getType())
                .group(group)
                .company(company)
                .openingBalance(request.getOpeningBalance())
                .currentBalance(request.getOpeningBalance())
                .gstNumber(request.getGstNumber())
                .contactNumber(request.getContactNumber())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();

        return toDTO(ledgerRepository.save(ledger));
    }

    public List<LedgerResponseDTO> getAllLedgers(Long companyId) {
        return ledgerRepository.findByCompanyIdAndIsDeletedFalse(companyId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<LedgerResponseDTO> getLedgersByType(Long companyId, Ledger.LedgerType type) {
        return ledgerRepository.findByCompanyIdAndTypeAndIsDeletedFalse(companyId, type)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public LedgerResponseDTO getLedgerById(Long id) {
        return toDTO(ledgerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Ledger not found")));
    }

    public LedgerResponseDTO updateLedger(Long id, LedgerRequestDTO request) {
        Ledger ledger = ledgerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Ledger not found"));

        LedgerGroup group = ledgerGroupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Ledger group not found"));

        ledger.setName(request.getName());
        ledger.setType(request.getType());
        ledger.setGroup(group);
        ledger.setGstNumber(request.getGstNumber());
        ledger.setContactNumber(request.getContactNumber());
        ledger.setEmail(request.getEmail());
        ledger.setAddress(request.getAddress());

        return toDTO(ledgerRepository.save(ledger));
    }

    public void deleteLedger(Long id) {
        Ledger ledger = ledgerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Ledger not found"));
        ledger.setDeleted(true);
        ledgerRepository.save(ledger);
    }
}
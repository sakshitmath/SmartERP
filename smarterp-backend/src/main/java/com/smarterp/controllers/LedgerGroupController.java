package com.smarterp.controllers;

import com.smarterp.entities.Company;
import com.smarterp.entities.LedgerGroup;
import com.smarterp.repositories.CompanyRepository;
import com.smarterp.repositories.LedgerGroupRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/api/ledger-groups")
@RequiredArgsConstructor
@Tag(name = "Ledger Groups", description = "Ledger Group APIs")
public class LedgerGroupController {

    private final LedgerGroupRepository ledgerGroupRepository;
    private final CompanyRepository companyRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestParam Long companyId,
                                    @RequestParam String name,
                                    @RequestParam LedgerGroup.GroupNature nature) {
        Company company = companyRepository.findByIdAndIsDeletedFalse(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Check if group already exists
        List<LedgerGroup> existing = ledgerGroupRepository.findByCompanyIdAndIsDeletedFalse(companyId);
        boolean alreadyExists = existing.stream()
                .anyMatch(g -> g.getName().equalsIgnoreCase(name));

        if (alreadyExists) {
            return ResponseEntity.badRequest().body(Map.of("error", "Group '" + name + "' already exists"));
        }

        LedgerGroup group = LedgerGroup.builder()
                .name(name)
                .nature(nature)
                .company(company)
                .build();

        LedgerGroup saved = ledgerGroupRepository.save(group);

        return ResponseEntity.ok(Map.of(
                "id", saved.getId(),
                "name", saved.getName(),
                "nature", saved.getNature()
        ));
    }
}
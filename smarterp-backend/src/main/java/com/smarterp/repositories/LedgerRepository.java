package com.smarterp.repositories;

import com.smarterp.entities.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {
    List<Ledger> findByCompanyIdAndIsDeletedFalse(Long companyId);
    List<Ledger> findByCompanyIdAndTypeAndIsDeletedFalse(Long companyId, Ledger.LedgerType type);
    Optional<Ledger> findByIdAndIsDeletedFalse(Long id);
}
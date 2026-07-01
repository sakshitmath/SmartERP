package com.smarterp.repositories;

import com.smarterp.entities.Ledger;
import com.smarterp.entities.LedgerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {
    List<Ledger> findByCompanyIdAndIsDeletedFalse(Long companyId);
    List<Ledger> findByCompanyIdAndTypeAndIsDeletedFalse(Long companyId, Ledger.LedgerType type);
    Optional<Ledger> findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT l FROM Ledger l WHERE l.company.id = :companyId AND l.group.nature = :nature AND l.isDeleted = false")
    List<Ledger> findByCompanyIdAndGroupNature(@Param("companyId") Long companyId, @Param("nature") LedgerGroup.GroupNature nature);
}
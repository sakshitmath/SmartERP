package com.smarterp.repositories;

import com.smarterp.entities.LedgerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerGroupRepository extends JpaRepository<LedgerGroup, Long> {
    List<LedgerGroup> findByCompanyIdAndIsDeletedFalse(Long companyId);
}
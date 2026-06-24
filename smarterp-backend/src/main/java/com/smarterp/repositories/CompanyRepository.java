package com.smarterp.repositories;

import com.smarterp.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByUserIdAndIsDeletedFalse(Long userId);
    Optional<Company> findByIdAndIsDeletedFalse(Long id);
    long countByUserIdAndIsDeletedFalse(Long userId);
}
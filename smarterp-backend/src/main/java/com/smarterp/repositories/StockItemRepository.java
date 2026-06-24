package com.smarterp.repositories;

import com.smarterp.entities.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    List<StockItem> findByCompanyIdAndIsDeletedFalse(Long companyId);
    Optional<StockItem> findByIdAndIsDeletedFalse(Long id);
    boolean existsBySkuAndIsDeletedFalse(String sku);
}
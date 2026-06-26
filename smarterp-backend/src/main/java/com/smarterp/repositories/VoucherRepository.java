package com.smarterp.repositories;

import com.smarterp.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    List<Voucher> findByCompanyIdAndVoucherTypeAndIsDeletedFalse(Long companyId, Voucher.VoucherType type);
    List<Voucher> findByCompanyIdAndIsDeletedFalse(Long companyId);
    Optional<Voucher> findByIdAndIsDeletedFalse(Long id);
    long countByCompanyIdAndVoucherType(Long companyId, Voucher.VoucherType type);
}
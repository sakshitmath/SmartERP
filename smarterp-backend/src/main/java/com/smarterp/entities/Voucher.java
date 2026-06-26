package com.smarterp.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vouchers")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoucherType voucherType;

    @Column(name = "voucher_number", unique = true)
    private String voucherNumber;

    @Column(name = "voucher_date", nullable = false)
    private LocalDate voucherDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_id")
    private Ledger ledger;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "total_gst", precision = 15, scale = 2)
    private BigDecimal totalGst = BigDecimal.ZERO;

    @Column(name = "grand_total", precision = 15, scale = 2)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VoucherItem> items;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum VoucherType {
        PURCHASE, SALES, PAYMENT, RECEIPT, JOURNAL, CONTRA
    }
}
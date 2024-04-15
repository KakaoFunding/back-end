package org.kakaoshare.backend.domain.receipt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        indexes = {@Index(name = "idx_receipt_option_receipt_id",columnList = "receipt_id",unique = true)}
)
public class ReceiptOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receipt_option_id;

    @Column(nullable = false)
    private String optionName;

    @Column(nullable = false)
    private String optionDetailName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;

    @Builder
    public ReceiptOption(final String optionName, final String optionDetailName) {
        this.optionName = optionName;
        this.optionDetailName = optionDetailName;
    }

    public void setReceipt(final Receipt receipt) {
        if (receipt != null) {
            this.receipt = receipt;
        }
    }
}

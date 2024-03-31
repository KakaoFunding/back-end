package org.kakaoshare.backend.domain.receipt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
package org.kakaoshare.backend.domain.funding.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.entity.Product;

import java.time.LocalDate;

import static org.kakaoshare.backend.domain.funding.entity.FundingStatus.CANCEL;
import static org.kakaoshare.backend.domain.funding.entity.FundingStatus.PROGRESS;


@Entity
@Getter
@AllArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_funding_member_id",columnList = "member_id",unique = true),
                @Index(name = "idx_funding_product_id",columnList = "product_id",unique = true),
        }
)
public class Funding extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundingId;

    @Builder.Default
    @Column(columnDefinition = "varchar(255)", nullable = false)
    @Enumerated(EnumType.STRING)
    private FundingStatus status = PROGRESS;
    
    @Column(nullable = false)
    private LocalDate expiredAt;
    
    @Column(nullable = false, precision = 12, scale = 2)
    private Long goalAmount;
    
    @ColumnDefault("0")
    @Column(nullable = false, precision = 12, scale = 2)
    private Long accumulateAmount = 0L;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Funding() {

    }

    @Builder
    public Funding(final Long fundingId,
                   final Member member,
                   final Product product,
                   final Long goalAmount,
                   final LocalDate expiredAt) {
        this.fundingId = fundingId;
        this.member = member;
        this.product = product;
        this.goalAmount = goalAmount;
        this.expiredAt = expiredAt;
    }

    public Funding(final Member member,
                   final Product product,
                   final Long goalAmount,
                   final LocalDate expiredAt) {
        this(null, member, product, goalAmount, expiredAt);
    }

    public void increaseAccumulateAmount(final Long amount) {
        if (amount != null) {
            this.accumulateAmount += amount;
        }
    }

    public void decreaseAccumulateAmount(final Long amount) {
        if (amount != null) {
            this.accumulateAmount -= amount;
        }
    }

    public void cancel() {
        this.status = CANCEL;
        this.accumulateAmount = 0L;
    }

    public boolean canceled() {
        return status.canceled();
    }

    @Override
    public String toString() {
        return "Funding{" +
                "fundingId=" + fundingId +
                ", status='" + status + '\'' +
                ", expiredAt=" + expiredAt +
                ", goalAmount=" + goalAmount +
                ", accumulateAmount=" + accumulateAmount +
                ", member=" + member +
                ", product=" + product +
                '}';
    }
}

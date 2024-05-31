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
import java.util.Optional;

import static org.kakaoshare.backend.domain.funding.entity.FundingStatus.BEFORE_PAYING_REMAINING;
import static org.kakaoshare.backend.domain.funding.entity.FundingStatus.CANCEL;
import static org.kakaoshare.backend.domain.funding.entity.FundingStatus.COMPLETE;
import static org.kakaoshare.backend.domain.funding.entity.FundingStatus.PROGRESS;

@Entity
@Getter
@AllArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_funding_member_id", columnList = "member_id"),
                @Index(name = "idx_funding_product_id", columnList = "product_id"),
        }
)
public class Funding extends BaseTimeEntity {
    private static final Long ZERO = 0L;
    private static final int SCALE = 4;
    private static final double DEFAULT_PROGRESS_RATE = 0.0;
    private static final double PERCENT_MULTIPLIER = 100.0;

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
        if (amount != null && !satisfiedAccumulateAmount()) {
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

    public void reflectStatus(final Long amount,
                              final Long contributorId) {
        if (!satisfiedAccumulateAmount()) {
            return;
        }

        this.status = BEFORE_PAYING_REMAINING;
        if (isPayRemainingByCreator(amount, contributorId) || hasNoRemainingPay()) {
            this.status = COMPLETE;
        }
    }

    public boolean attributable() {
        return status.attributable();
    }

    public boolean isAttributableAmount(final int attributeAmount) {
        return getRemainAmount() >= attributeAmount;
    }

    public boolean canceled() {
        return status.canceled();
    }

    public boolean completed() {
        return status.completed();
    }

    public boolean satisfiedAccumulateAmount() {
        return goalAmount.equals(accumulateAmount);
    }
    public Long calculateRemainAmount(){
        return goalAmount - accumulateAmount;
    }
    public Double calculateProgressRate() {
        return Optional.of(goalAmount)
                .filter(goalAmountValue -> goalAmountValue.compareTo(ZERO) != 0)
                .map(goalAmountValue -> divide(accumulateAmount, goalAmountValue) * PERCENT_MULTIPLIER)
                .orElse(DEFAULT_PROGRESS_RATE);
    }

    private static Double divide(long numerator, long denominator) {
        long scaledNumerator = numerator * (long) Math.pow(10, Funding.SCALE + 1);
        long rawResult = scaledNumerator / denominator;
        long remainder = rawResult % 10;
        rawResult /= 10;
        if (remainder >= 5) {
            rawResult += 1;
        }
        return rawResult / Math.pow(10, Funding.SCALE);
    }
    private long getRemainAmount() {
        if (satisfiedAccumulateAmount()) {
            return product.getPrice() - goalAmount;
        }

        return goalAmount - accumulateAmount;
    }

    private boolean hasNoRemainingPay() {
        return product.getPrice().equals(goalAmount);
    }

    private boolean isPayRemainingByCreator(final Long amount,
                                            final Long contributorId) {
        final Long creatorId = member.getMemberId();
        if (!creatorId.equals(contributorId)) {
            return false;
        }

        return product.getPrice() == amount + goalAmount;
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

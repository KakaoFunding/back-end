package org.kakaoshare.backend.domain.funding.entity;

import jakarta.persistence.CascadeType;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.payment.entity.Payment;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        indexes = {@Index(name = "idx_funding_detail_funding_id", columnList = "funding_id")}
)
public class FundingDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundingDetailId;

    @Column(nullable = false)
    private Long amount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Double rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id", nullable = false)
    private Funding funding;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    public FundingDetail(final Member member,
                         final Funding funding,
                         final Payment payment) {
        this.member = member;
        this.amount = payment.getTotalPrice();
        this.rate = calculateRate(this.amount);
        this.funding = funding;
        this.payment = payment;
    }

    public void increaseAmountAndRate(final Long amount) {
        if (amount != null) {
            this.rate += calculateRate(amount);
            this.amount += amount;
        }
    }

    private double calculateRate(final Long amount) {
        return 100. * amount / funding.getGoalAmount();
    }

    @Override
    public String toString() {
        return "FundingDetail{" +
                "fundingDetailId=" + fundingDetailId +
                ", amount=" + amount +
                ", rate=" + rate +
                ", funding=" + funding +
                ", payment=" + payment +
                '}';
    }
}

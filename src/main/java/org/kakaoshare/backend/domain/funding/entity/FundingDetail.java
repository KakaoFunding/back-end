package org.kakaoshare.backend.domain.funding.entity;

import jakarta.persistence.CascadeType;
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
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.payment.entity.Payment;

import static org.kakaoshare.backend.domain.funding.entity.FundingDetailStatus.CANCEL_REFUND;
import static org.kakaoshare.backend.domain.funding.entity.FundingDetailStatus.PROGRESS;


@Entity
@Getter
@Table(
        indexes = {@Index(name = "idx_funding_detail_funding_id", columnList = "funding_id")}
)
public class FundingDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundingDetailId;

    @Column(nullable = false)
    private Long amount;

    @Builder.Default
    @Column(columnDefinition = "varchar(255)", nullable = false)
    @Enumerated(EnumType.STRING)
    private FundingDetailStatus status = PROGRESS;

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

    protected FundingDetail() {

    }

    public FundingDetail(final Member member,
                         final Funding funding,
                         final Payment payment) {
        this.member = member;
        this.amount = payment.getTotalPrice();
        this.funding = funding;
        this.payment = payment;
        this.rate = calculateRate(this.amount);
    }

    public void increaseAmountAndRate(final Long amount) {
        if (amount != null) {
            this.rate += calculateRate(amount);
            this.amount += amount;
        }
    }

    public void partialCancel(final Long amount) {
        this.rate -= calculateRate(amount);
        this.amount -= amount;
        payment.partialCancel(amount);
    }

    public void cancel() {
        // TODO: 4/27/24 상태, 기여도만 수정하고 기여 금액은 남김
        this.status = CANCEL_REFUND;
        this.rate = 0.;
    }

    public boolean canceled() {
        return status.canceled();
    }

    private double calculateRate(final Long amount) {
        return 100. * amount / funding.getGoalAmount();
    }

    @Override
    public String toString() {
        return "FundingDetail{" +
                "fundingDetailId=" + fundingDetailId +
                ", amount=" + amount +
                ", status=" + status +
                ", member=" + member +
                ", rate=" + rate +
                ", funding=" + funding +
                ", payment=" + payment +
                '}';
    }
}

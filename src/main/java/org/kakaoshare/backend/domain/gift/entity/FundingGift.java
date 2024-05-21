package org.kakaoshare.backend.domain.gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import org.kakaoshare.backend.domain.base.entity.BaseTimeEntity;
import org.kakaoshare.backend.domain.funding.entity.Funding;

import java.time.LocalDateTime;

import static org.kakaoshare.backend.domain.gift.entity.GiftStatus.NOT_USED;

@Entity
@Getter
public class FundingGift extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundingGiftId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id", nullable = false)
    private Funding funding;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private GiftStatus status = NOT_USED;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    protected FundingGift() {

    }

    @Builder
    public FundingGift(final Funding funding,
                       final LocalDateTime expiredAt) {
        validateFunding(funding);
        validateExpiredAt(expiredAt);
        this.funding = funding;
        this.expiredAt = expiredAt;
    }

    private void validateFunding(final Funding funding) {
        if (!funding.completed()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExpiredAt(final LocalDateTime expiredAt) {
        if (!expiredAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException();
        }
    }
}

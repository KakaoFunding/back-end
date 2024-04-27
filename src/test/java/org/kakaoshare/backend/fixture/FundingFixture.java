package org.kakaoshare.backend.fixture;

import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.entity.Product;

import java.time.LocalDate;

public enum FundingFixture {
    SAMPLE_FUNDING(LocalDate.now().plusDays(30), 1_000L, 500L);
    private final LocalDate expiredAt;
    private final Long goalAmount;
    private final Long accumulateAmount;

    FundingFixture(final LocalDate expiredAt,
                   final Long goalAmount,
                   final Long accumulateAmount) {
        this.expiredAt = expiredAt;
        this.goalAmount = goalAmount;
        this.accumulateAmount = accumulateAmount;
    }

    public Funding 생성(final Member member,
                      final Product product) {
        final Funding funding = Funding.builder()
                .expiredAt(expiredAt)
                .goalAmount(goalAmount)
                .member(member)
                .product(product)
                .build();
        funding.increaseAccumulateAmount(accumulateAmount);
        return funding;
    }

    public Funding 생성(final Long fundingId,
                      final Member member,
                      final Product product) {
        final Funding funding = Funding.builder()
                .fundingId(fundingId)
                .expiredAt(expiredAt)
                .goalAmount(goalAmount)
                .member(member)
                .product(product)
                .build();
        funding.increaseAccumulateAmount(accumulateAmount);
        return funding;
    }
}

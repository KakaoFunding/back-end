package org.kakaoshare.backend.fixture;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.product.entity.Product;

public enum FundingFixture {
    SAMPLE_FUNDING("ACTIVE", LocalDate.now().plusDays(30), new BigDecimal("1000.00"), new BigDecimal("500.00"));

    private final String status;
    private final LocalDate expiredAt;
    private final BigDecimal goalAmount;
    private final BigDecimal accumulateAmount;

    FundingFixture(final String status,
                   final LocalDate expiredAt,
                   final BigDecimal goalAmount,
                   final BigDecimal accumulateAmount) {
        this.status = status;
        this.expiredAt = expiredAt;
        this.goalAmount = goalAmount;
        this.accumulateAmount = accumulateAmount;
    }

    public Funding 생성(Member member, Product product) {
        return Funding.builder()
                .status(this.status)
                .expiredAt(this.expiredAt)
                .goalAmount(this.goalAmount)
                .accumulateAmount(this.accumulateAmount)
                .member(member)
                .product(product)
                .build();
    }
}

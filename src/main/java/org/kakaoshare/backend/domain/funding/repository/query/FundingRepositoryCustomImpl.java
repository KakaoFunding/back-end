package org.kakaoshare.backend.domain.funding.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.entity.QFunding;

@RequiredArgsConstructor
public class FundingRepositoryCustomImpl implements FundingRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Funding> findByIdAndMemberId(Long fundingId, Long memberId) {
        Funding funding = queryFactory
                .selectFrom(QFunding.funding)
                .where(QFunding.funding.fundingId.eq(fundingId)
                        .and(QFunding.funding.member.memberId.eq(memberId)))
                .fetchOne();
        return Optional.ofNullable(funding);
    }
}

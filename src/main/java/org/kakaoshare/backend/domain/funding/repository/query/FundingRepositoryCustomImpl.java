package org.kakaoshare.backend.domain.funding.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.entity.QFunding;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Component
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

    @Override
    public List<Funding> findAllByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(QFunding.funding)
                .where(QFunding.funding.member.memberId.eq(memberId))
                .fetch();
    }

    @Override
    public Slice<Funding> findFundingByMemberIdWithSlice(Long memberId, Pageable pageable) {
        List<Funding> content = queryFactory
                .selectFrom(QFunding.funding)
                .where(QFunding.funding.member.memberId.eq(memberId))
                .orderBy(QFunding.funding.fundingId.desc()) // 예시로 ID 내림차순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // 다음 페이지 존재 여부 확인을 위해 1 추가
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(content.size() - 1);
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }
}

package org.kakaoshare.backend.domain.funding.repository.query;

import static org.kakaoshare.backend.common.util.sort.SortUtil.MOST_RECENT;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

import org.kakaoshare.backend.common.util.RepositoryUtils;
import org.kakaoshare.backend.common.util.sort.SortUtil;
import org.kakaoshare.backend.common.util.sort.SortableRepository;
import org.kakaoshare.backend.domain.funding.dto.FundingResponse;
import org.kakaoshare.backend.domain.funding.entity.Funding;
import org.kakaoshare.backend.domain.funding.entity.FundingStatus;
import org.kakaoshare.backend.domain.funding.entity.QFunding;
import org.kakaoshare.backend.domain.product.entity.QProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FundingRepositoryCustomImpl implements FundingRepositoryCustom, SortableRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<FundingResponse> findByMemberId(Long memberId) {
        List<Funding> fundings = queryFactory
                .selectFrom(QFunding.funding)
                .where(QFunding.funding.member.memberId.eq(memberId))
                .fetch();

        return fundings.stream()
                .map(FundingResponse::from)
                .toList();

    }

    @Override
    public Optional<Funding> findByIdAndMemberId(Long fundingId, Long memberId) {
        Funding funding = queryFactory
                .selectFrom(QFunding.funding)
                .where(fundingAndMemberPredicate(fundingId, memberId))
                .fetchOne();
        return Optional.ofNullable(funding);
    }

    private BooleanExpression fundingAndMemberPredicate(Long fundingId, Long memberId) {
        return QFunding.funding.fundingId.eq(fundingId)
                .and(QFunding.funding.member.memberId.eq(memberId));
    }

    @Override
    public List<Funding> findAllByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(QFunding.funding)
                .where(QFunding.funding.member.memberId.eq(memberId))
                .fetch();
    }

    @Override
    public Page<FundingResponse> findFundingByMemberIdAndStatusWithPage(Long memberId, FundingStatus status,
                                                                        Pageable pageable) {
        JPAQuery<FundingResponse> contentQuery = queryFactory
                .select(Projections.constructor(
                        FundingResponse.class,
                        QFunding.funding.fundingId,
                        QFunding.funding.status.stringValue(),
                        QFunding.funding.expiredAt,
                        QFunding.funding.goalAmount,
                        QFunding.funding.product.brandName,
                        QFunding.funding.product.name,
                        QFunding.funding.product.photo
                ))
                .from(QFunding.funding)
                .leftJoin(QFunding.funding.product)
                .on(QFunding.funding.product.productId.eq(QProduct.product.productId))
                .where(QFunding.funding.member.memberId.eq(memberId)
                        .and(QFunding.funding.status.eq(status)))
                .orderBy(SortUtil.from(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = queryFactory
                .select(QFunding.funding.count())
                .from(QFunding.funding)
                .where(QFunding.funding.member.memberId.eq(memberId)
                        .and(QFunding.funding.status.eq(status)));

        return RepositoryUtils.toPage(pageable, contentQuery, countQuery);
    }


    @Override
    public OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        return Stream.concat(Stream.of(SortUtil.from(pageable)), Stream.of(MOST_RECENT)).toArray(OrderSpecifier[]::new);
    }
}

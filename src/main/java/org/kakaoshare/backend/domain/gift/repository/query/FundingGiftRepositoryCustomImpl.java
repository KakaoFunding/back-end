package org.kakaoshare.backend.domain.gift.repository.query;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.gift.dto.funding.inquiry.response.FundingGiftHistoryResponse;
import org.kakaoshare.backend.domain.gift.dto.funding.inquiry.response.QFundingGiftHistoryResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.kakaoshare.backend.domain.product.dto.QProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.kakaoshare.backend.common.util.RepositoryUtils.containsExpression;
import static org.kakaoshare.backend.common.util.RepositoryUtils.createOrderSpecifiers;
import static org.kakaoshare.backend.common.util.RepositoryUtils.eqExpression;
import static org.kakaoshare.backend.common.util.RepositoryUtils.toPage;
import static org.kakaoshare.backend.domain.funding.entity.QFunding.funding;
import static org.kakaoshare.backend.domain.gift.entity.QFundingGift.fundingGift;
import static org.kakaoshare.backend.domain.member.entity.QMember.member;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;

@RequiredArgsConstructor
public class FundingGiftRepositoryCustomImpl implements FundingGiftRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FundingGiftHistoryResponse> findHistoryByCondition(final String providerId, final List<GiftStatus> statuses, final Pageable pageable) {
        final JPAQuery<FundingGiftHistoryResponse> contentQuery = createHistoryContentQuery(providerId, statuses, pageable);
        final JPAQuery<Long> countQuery = createHistoryCountQuery(providerId, statuses, pageable);
        return toPage(pageable, contentQuery, countQuery);
    }

    @Override
    public Page<FundingGiftHistoryResponse> findHistoryByConditionWithoutStatus(final String providerId, final Pageable pageable) {
        // TODO 5/28 eqExpression()에서 2번째 파라미터가 null인 경우 true고 간주하여 status에 null을 대입
        final JPAQuery<FundingGiftHistoryResponse> contentQuery = createHistoryContentQuery(providerId, null, pageable);
        final JPAQuery<Long> countQuery = createHistoryCountQuery(providerId, null, pageable);
        return toPage(pageable, contentQuery, countQuery);
    }

    private JPAQuery<Long> createHistoryCountQuery(final String providerId, final List<GiftStatus> statuses, final Pageable pageable) {
        return createHistoryBaseQuery(providerId, statuses, pageable)
                .select(fundingGift.count());
    }

    private JPAQuery<FundingGiftHistoryResponse> createHistoryContentQuery(final String providerId, final List<GiftStatus> statuses, final Pageable pageable) {
        return createHistoryBaseQuery(providerId, statuses, pageable)
                .select(getFundingGiftHistoryResponse())
                .orderBy(createOrderSpecifiers(fundingGift, pageable));
    }

    private JPAQuery<?> createHistoryBaseQuery(final String providerId, final List<GiftStatus> statuses, final Pageable pageable) {
        return queryFactory.from(fundingGift)
                .innerJoin(fundingGift.funding, funding)
                .innerJoin(funding.member, member)
                .where(
                        eqExpression(member.providerId, providerId),
                        containsExpression(fundingGift.status, statuses)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    private QFundingGiftHistoryResponse getFundingGiftHistoryResponse() {
        // TODO 5/28 펀딩 등록시 수량이 고려되지 않아 수량을 1로 고정
        return new QFundingGiftHistoryResponse(fundingGift.fundingGiftId, getProductDto(), Expressions.ONE, fundingGift.createdAt);
    }

    private QProductDto getProductDto() {
        return new QProductDto(
                product.productId,
                product.name,
                product.photo,
                product.price,
                product.brandName
        );
    }
}

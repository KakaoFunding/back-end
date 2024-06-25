package org.kakaoshare.backend.domain.funding.repository.query;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.vo.date.Date;
import org.kakaoshare.backend.domain.funding.dto.inquiry.ContributedFundingHistoryDto;
import org.kakaoshare.backend.domain.funding.dto.inquiry.QContributedFundingHistoryDto;
import org.kakaoshare.backend.domain.member.entity.QMember;
import org.kakaoshare.backend.domain.product.dto.QProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.kakaoshare.backend.common.util.RepositoryUtils.createOrderSpecifiers;
import static org.kakaoshare.backend.common.util.RepositoryUtils.eqExpression;
import static org.kakaoshare.backend.common.util.RepositoryUtils.periodExpression;
import static org.kakaoshare.backend.common.util.RepositoryUtils.toPage;
import static org.kakaoshare.backend.domain.funding.entity.QFunding.funding;
import static org.kakaoshare.backend.domain.funding.entity.QFundingDetail.fundingDetail;
import static org.kakaoshare.backend.domain.member.entity.QMember.member;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;

@RequiredArgsConstructor
public class FundingDetailRepositoryCustomImpl implements FundingDetailRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ContributedFundingHistoryDto> findHistoryByCondition(final String providerId, final Date date, final String status, final Pageable pageable) {
        final JPAQuery<Long> countQuery = createCountQuery(providerId, date, status, pageable);
        final JPAQuery<ContributedFundingHistoryDto> contentQuery = createContentQuery(providerId, date, status, pageable);
        return toPage(pageable, contentQuery, countQuery);
    }

    @Override
    public Page<ContributedFundingHistoryDto> findHistoryByConditionWithoutStatus(final String providerId, final Date date, final Pageable pageable) {
        final JPAQuery<Long> countQuery = createCountQuery(providerId, date, pageable);
        final JPAQuery<ContributedFundingHistoryDto> contentQuery = createContentQuery(providerId, date, pageable);
        return toPage(pageable, contentQuery, countQuery);
    }

    private JPAQuery<?> createBaseQuery(final String providerId, final Date date) {
        return queryFactory
                .from(fundingDetail)
                .innerJoin(fundingDetail.funding, funding)
                .innerJoin(funding.member, new QMember("creator"))
                .innerJoin(fundingDetail.member, member)
                .innerJoin(funding.product, product)
                .where(
                        eqExpression(member.providerId, providerId),
                        periodExpression(fundingDetail.createdAt, date)
                );
    }

    private JPAQuery<?> createBaseQuery(final String providerId, final Date date, final String status) {
        return createBaseQuery(providerId, date)
                .where(eqExpression(fundingDetail.status.stringValue(), status));
    }

    private JPAQuery<Long> createCountQuery(final String providerId,
                                            final Date date,
                                            final String status,
                                            final Pageable pageable) {
        return createBaseQuery(providerId, date, status)
                .select(funding.count())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    private JPAQuery<Long> createCountQuery(final String providerId,
                                            final Date date,
                                            final Pageable pageable) {
        return createBaseQuery(providerId, date)
                .select(funding.count())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    private JPAQuery<ContributedFundingHistoryDto> createContentQuery(final String providerId,
                                                                      final Date date,
                                                                      final Pageable pageable) {
        return createBaseQuery(providerId, date)
                .select(getContributedFundingHistoryDto())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(createOrderSpecifiers(fundingDetail, pageable));
    }

    private JPAQuery<ContributedFundingHistoryDto> createContentQuery(final String providerId,
                                                                           final Date date,
                                                                           final String status,
                                                                           final Pageable pageable) {
        return createBaseQuery(providerId, date, status)
                .select(getContributedFundingHistoryDto())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(createOrderSpecifiers(fundingDetail, pageable));
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

    private QContributedFundingHistoryDto getContributedFundingHistoryDto() {
        return new QContributedFundingHistoryDto(
                getProductDto(),
                funding.fundingId,
                fundingDetail.fundingDetailId,
                fundingDetail.amount,
                fundingDetail.createdAt,
                member.providerId,
                member.name,
                fundingDetail.status.stringValue()
        );
    }
}

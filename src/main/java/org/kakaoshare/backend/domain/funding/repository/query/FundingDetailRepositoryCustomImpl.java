package org.kakaoshare.backend.domain.funding.repository.query;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.vo.Date;
import org.kakaoshare.backend.domain.funding.dto.inquiry.ContributedFundingHistoryResponse;
import org.kakaoshare.backend.domain.funding.dto.inquiry.QContributedFundingHistoryDto;
import org.kakaoshare.backend.domain.funding.dto.inquiry.QContributedFundingHistoryResponse;
import org.kakaoshare.backend.domain.member.entity.QMember;
import org.kakaoshare.backend.domain.product.dto.QProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.kakaoshare.backend.common.util.RepositoryUtils.*;
import static org.kakaoshare.backend.domain.funding.entity.QFunding.funding;
import static org.kakaoshare.backend.domain.funding.entity.QFundingDetail.fundingDetail;
import static org.kakaoshare.backend.domain.member.entity.QMember.member;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;

@RequiredArgsConstructor
public class FundingDetailRepositoryCustomImpl implements FundingDetailRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ContributedFundingHistoryResponse> findHistoryByCondition(final String providerId, final Date date, final String status, final Pageable pageable) {
        final JPAQuery<Long> countQuery = createCountQuery(providerId, date, status, pageable);
        final JPAQuery<ContributedFundingHistoryResponse> contentQuery = createContentQuery(providerId, date, status, pageable);
        return toPage(pageable, contentQuery, countQuery);
    }

    @Override
    public Page<ContributedFundingHistoryResponse> findHistoryByConditionWithoutStatus(final String providerId, final Date date, final Pageable pageable) {
        final JPAQuery<Long> countQuery = createCountQuery(providerId, date, pageable);
        final JPAQuery<ContributedFundingHistoryResponse> contentQuery = createContentQuery(providerId, date, pageable);
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

    private JPAQuery<ContributedFundingHistoryResponse> createContentQuery(final String providerId,
                                                                           final Date date,
                                                                           final Pageable pageable) {
        return createBaseQuery(providerId, date)
                .select(new QContributedFundingHistoryResponse(getProductDto(), getContributedFundingHistoryDto()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(createOrderSpecifiers(fundingDetail, pageable));
    }

    private JPAQuery<ContributedFundingHistoryResponse> createContentQuery(final String providerId,
                                                                           final Date date,
                                                                           final String status,
                                                                           final Pageable pageable) {
        return createBaseQuery(providerId, date, status)
                .select(new QContributedFundingHistoryResponse(getProductDto(), getContributedFundingHistoryDto()))
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
                funding.fundingId,
                fundingDetail.fundingDetailId,
                fundingDetail.amount,
                fundingDetail.createdAt,
                member.name,
                fundingDetail.status.stringValue()
        );
    }
}

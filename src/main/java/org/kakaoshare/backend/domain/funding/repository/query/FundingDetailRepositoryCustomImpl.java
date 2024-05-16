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
    public Page<ContributedFundingHistoryResponse> findHistoryByProviderIdAndDateAndStatus(final String providerId, final Date date, final String status, final Pageable pageable) {
        final JPAQuery<Long> countQuery = queryFactory.select(fundingDetail.count())
                .from(fundingDetail)
                .innerJoin(fundingDetail.funding, funding)
                .innerJoin(funding.member, new QMember("creator"))
                .innerJoin(fundingDetail.member, member)
                .innerJoin(funding.product, product)
                .where(
                        eqExpression(member.providerId, providerId),
                        eqExpression(fundingDetail.status.stringValue(), status),
                        periodExpression(fundingDetail.createdAt, date)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        final JPAQuery<ContributedFundingHistoryResponse> contentQuery = queryFactory.select(new QContributedFundingHistoryResponse(getProductDto(), getContributedFundingHistoryDto()))
                .from(fundingDetail)
                .innerJoin(fundingDetail.funding, funding)
                .innerJoin(funding.member, new QMember("creator"))
                .innerJoin(fundingDetail.member, member)
                .innerJoin(funding.product, product)
                .where(
                        eqExpression(member.providerId, providerId),
                        eqExpression(fundingDetail.status.stringValue(), status),
                        periodExpression(fundingDetail.createdAt, date)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(createOrderSpecifiers(fundingDetail, pageable));

        return toPage(pageable, contentQuery, countQuery);
    }

    @Override
    public Page<ContributedFundingHistoryResponse> findHistoryByProviderIdAndDate(final String providerId, final Date date, final Pageable pageable) {
        final JPAQuery<Long> countQuery = queryFactory.select(fundingDetail.count())
                .from(fundingDetail)
                .innerJoin(fundingDetail.funding, funding)
                .innerJoin(funding.member, new QMember("creator"))
                .innerJoin(fundingDetail.member, member)
                .innerJoin(funding.product, product)
                .where(
                        eqExpression(member.providerId, providerId),
                        periodExpression(fundingDetail.createdAt, date)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        final JPAQuery<ContributedFundingHistoryResponse> contentQuery = queryFactory.select(new QContributedFundingHistoryResponse(getProductDto(), getContributedFundingHistoryDto()))
                .from(fundingDetail)
                .innerJoin(fundingDetail.funding, funding)
                .innerJoin(funding.member, new QMember("creator"))
                .innerJoin(fundingDetail.member, member)
                .innerJoin(funding.product, product)
                .where(
                        eqExpression(member.providerId, providerId),
                        periodExpression(fundingDetail.createdAt, date)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(createOrderSpecifiers(fundingDetail, pageable));

        return toPage(pageable, contentQuery, countQuery);
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
                fundingDetail.createdAt,
                member.name,
                fundingDetail.status.stringValue()
        );
    }
}

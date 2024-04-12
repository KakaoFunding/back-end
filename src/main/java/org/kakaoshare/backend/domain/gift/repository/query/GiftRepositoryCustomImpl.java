package org.kakaoshare.backend.domain.gift.repository.query;

import static org.kakaoshare.backend.common.util.RepositoryUtils.toPage;
import static org.kakaoshare.backend.domain.gift.entity.QGift.gift;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.kakaoshare.backend.domain.product.entity.QProductThumbnail.productThumbnail;
import static org.kakaoshare.backend.domain.receipt.entity.QReceipt.receipt;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.gift.dto.GiftResponse;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GiftRepositoryCustomImpl implements GiftRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<GiftResponse> findGiftsByMemberIdAndStatus(Long memberId, GiftStatus status, Pageable pageable) {
        JPAQuery<GiftResponse> contentQuery = queryFactory
                .select(Projections.constructor(GiftResponse.class,
                        gift.giftId,
                        gift.expiredAt,
                        receipt.recipient.name,
                        JPAExpressions.select(product.name)
                                .from(product)
                                .where(product.productId.eq(receipt.product.productId)),
                        JPAExpressions.select(productThumbnail.thumbnailUrl)
                                .from(productThumbnail)
                                .where(productThumbnail.product.productId.eq(receipt.product.productId))
                                .limit(1),
                        receipt.product.brandName))
                .from(gift)
                .leftJoin(gift.receipt, receipt)
                .where(gift.status.eq(status)
                        .and(receipt.recipient.memberId.eq(memberId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = queryFactory
                .select(gift.count())
                .from(gift)
                .where(gift.status.eq(status)
                        .and(receipt.recipient.memberId.eq(memberId)));

        return toPage(pageable, contentQuery, countQuery);

    }

    @Override
    public Page<GiftResponse> findGiftsByMemberIdAndOtherStatuses(Long memberId, Pageable pageable) {
        List<GiftResponse> content = queryFactory
                .select(Projections.constructor(GiftResponse.class,
                        gift.giftId,
                        gift.expiredAt,
                        gift.receipt.recipient.name.as("recipientName"),
                        gift.receipt.product.name.as("productName"),
                        gift.receipt.product.productThumbnails.any().thumbnailUrl.as("productThumbnail"),
                        gift.receipt.product.brandName))
                .from(gift)
                .leftJoin(gift.receipt).fetchJoin()
                .where(gift.status.ne(GiftStatus.NOT_USED)
                        .and(gift.receipt.recipient.memberId.eq(memberId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(gift)
                .where(gift.status.ne(GiftStatus.NOT_USED)
                        .and(gift.receipt.recipient.memberId.eq(memberId)))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

}

package org.kakaoshare.backend.domain.gift.repository.query;

import static org.kakaoshare.backend.domain.gift.entity.QGift.gift;

import com.querydsl.core.types.Projections;
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
    public Page<GiftResponse> findGifts(Pageable pageable) {
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
                .where(gift.status.eq(GiftStatus.NOT_USED))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(gift)
                .where(gift.status.eq(GiftStatus.NOT_USED))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }
}

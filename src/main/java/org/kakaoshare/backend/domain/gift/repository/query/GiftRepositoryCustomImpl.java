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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.gift.dto.GiftDescriptionResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftDetailResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftResponse;
import org.kakaoshare.backend.domain.gift.entity.Gift;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.kakaoshare.backend.domain.gift.entity.QGift;
import org.kakaoshare.backend.domain.gift.exception.GiftErrorCode;
import org.kakaoshare.backend.domain.gift.exception.GiftException;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.ProductThumbnail;
import org.kakaoshare.backend.domain.product.entity.QProduct;
import org.kakaoshare.backend.domain.product.entity.QProductThumbnail;
import org.kakaoshare.backend.domain.product.exception.ProductErrorCode;
import org.kakaoshare.backend.domain.product.exception.ProductException;
import org.kakaoshare.backend.domain.receipt.entity.QReceipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class GiftRepositoryCustomImpl implements GiftRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public GiftDetailResponse findGiftDetailById(Long giftId) {
        Gift gift = findGiftById(giftId);

        Product product = Optional.ofNullable(queryFactory
                        .selectFrom(QProduct.product)
                        .where(QProduct.product.productId.eq(gift.getReceipt().getProduct().getProductId()))
                        .fetchOne())
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT_ERROR));

        return GiftDetailResponse.of(gift, product);
    }

    @Override
    public GiftDescriptionResponse findGiftDescriptionById(Long giftId) {
        Gift gift = findGiftById(giftId);

        Product product = Optional.ofNullable(queryFactory
                        .selectFrom(QProduct.product)
                        .where(QProduct.product.productId.eq(gift.getReceipt().getProduct().getProductId()))
                        .fetchOne())
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT_ERROR));

        ProductThumbnail productThumbnail = Optional.ofNullable(queryFactory
                        .selectFrom(QProductThumbnail.productThumbnail)
                        .where(QProductThumbnail.productThumbnail.product.productId.eq(product.getProductId()))
                        .fetchOne())
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND_THUMBNAIL_ERROR));

        return GiftDescriptionResponse.of(product, productThumbnail);
    }

    private Gift findGiftById(Long giftId) {
        return Optional.ofNullable(queryFactory
                        .selectFrom(gift)
                        .where(gift.giftId.eq(giftId)).fetchOne())
                .orElseThrow(() -> new GiftException(GiftErrorCode.NOT_FOUND_GIFT));
    }

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
}

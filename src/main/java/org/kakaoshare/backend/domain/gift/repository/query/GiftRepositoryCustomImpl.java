package org.kakaoshare.backend.domain.gift.repository.query;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.gift.dto.inquiry.detail.response.GiftDescriptionResponse;
import org.kakaoshare.backend.domain.gift.dto.inquiry.detail.response.GiftDetailResponse;
import org.kakaoshare.backend.domain.gift.dto.inquiry.history.GiftDto;
import org.kakaoshare.backend.domain.gift.dto.inquiry.history.QGiftDto;
import org.kakaoshare.backend.domain.gift.entity.Gift;
import org.kakaoshare.backend.domain.gift.entity.GiftStatus;
import org.kakaoshare.backend.domain.gift.exception.GiftErrorCode;
import org.kakaoshare.backend.domain.gift.exception.GiftException;
import org.kakaoshare.backend.domain.member.entity.QMember;
import org.kakaoshare.backend.domain.product.dto.QProductDto;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.ProductThumbnail;
import org.kakaoshare.backend.domain.product.entity.QProduct;
import org.kakaoshare.backend.domain.product.entity.QProductThumbnail;
import org.kakaoshare.backend.domain.product.exception.ProductErrorCode;
import org.kakaoshare.backend.domain.product.exception.ProductException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.kakaoshare.backend.common.util.RepositoryUtils.createOrderSpecifiers;
import static org.kakaoshare.backend.common.util.RepositoryUtils.eqExpression;
import static org.kakaoshare.backend.common.util.RepositoryUtils.toPage;
import static org.kakaoshare.backend.domain.gift.entity.QGift.gift;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.kakaoshare.backend.domain.receipt.entity.QReceipt.receipt;


@Repository
@RequiredArgsConstructor
public class GiftRepositoryCustomImpl implements GiftRepositoryCustom {
    private static final QMember receiver = new QMember("receiver");
    private static final QMember recipient = new QMember("recipient");

    private final JPAQueryFactory queryFactory;

    @Override
    public GiftDetailResponse findGiftDetailById(Long giftId) {
        Gift gift = findGiftById(giftId);

        Product product = Optional.ofNullable(queryFactory
                        .selectFrom(QProduct.product)
                        .where(QProduct.product.productId.eq(gift.getReceipt().getProduct().getProductId()))
                        .fetchOne())
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND));

        return GiftDetailResponse.of(gift, product);
    }

    @Override
    public GiftDescriptionResponse findGiftDescriptionById(Long giftId) {
        Gift gift = findGiftById(giftId);

        Product product = Optional.ofNullable(queryFactory
                        .selectFrom(QProduct.product)
                        .where(QProduct.product.productId.eq(gift.getReceipt().getProduct().getProductId()))
                        .fetchOne())
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND));

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
                .orElseThrow(() -> new GiftException(GiftErrorCode.NOT_FOUND));
    }

    @Override
    public Page<GiftDto> findHistoryByProviderIdAndStatus(final String providerId, final GiftStatus status, final Pageable pageable) {
        final JPAQuery<Long> countQuery = createHistoryCountQuery(providerId, status, pageable);
        final JPAQuery<GiftDto> contentQuery = createHistoryContentQuery(providerId, status, pageable);
        return toPage(pageable, contentQuery, countQuery);
    }

    private JPAQuery<?> createHistoryBaseQuery(final String providerId, final GiftStatus status) {
        return queryFactory
                .from(gift)
                .innerJoin(gift.receipt, receipt)
                .innerJoin(receipt.product, product)
                .innerJoin(receipt.recipient, recipient)
                .innerJoin(receipt.receiver, receiver)
                .where(
                        eqExpression(receiver.providerId, providerId),
                        eqExpression(gift.status, status)
                );
    }

    private JPAQuery<Long> createHistoryCountQuery(final String providerId, final GiftStatus status, final Pageable pageable) {
        return createHistoryBaseQuery(providerId, status)
                .select(gift.count())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    private JPAQuery<GiftDto> createHistoryContentQuery(final String providerId, final GiftStatus status, final Pageable pageable) {
        return createHistoryBaseQuery(providerId, status)
                .select(getGiftDto())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(createOrderSpecifiers(gift, pageable));
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

    private QGiftDto getGiftDto() {
        return new QGiftDto(
                gift.giftId,
                gift.expiredAt,
                gift.createdAt,
                recipient.name,
                recipient.providerId,
                getProductDto()
        );
    }

}

package org.kakaoshare.backend.domain.gift.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.error.GlobalErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;
import org.kakaoshare.backend.domain.gift.dto.GiftDescriptionResponse;
import org.kakaoshare.backend.domain.gift.dto.GiftDetailResponse;
import org.kakaoshare.backend.domain.gift.entity.Gift;
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
                        .selectFrom(QGift.gift)
                        .where(QGift.gift.giftId.eq(giftId)).fetchOne())
                .orElseThrow(() -> new GiftException(GiftErrorCode.NOT_FOUND_GIFT));
    }
}

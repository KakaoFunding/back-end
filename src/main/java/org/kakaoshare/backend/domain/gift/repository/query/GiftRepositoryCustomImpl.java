package org.kakaoshare.backend.domain.gift.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.common.error.GlobalErrorCode;
import org.kakaoshare.backend.common.error.exception.BusinessException;
import org.kakaoshare.backend.domain.gift.dto.GiftDetailResponse;
import org.kakaoshare.backend.domain.gift.entity.Gift;
import org.kakaoshare.backend.domain.gift.entity.QGift;
import org.kakaoshare.backend.domain.product.dto.DetailResponse;
import org.kakaoshare.backend.domain.product.entity.Product;
import org.kakaoshare.backend.domain.product.entity.QProduct;

@RequiredArgsConstructor
public class GiftRepositoryCustomImpl implements GiftRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public GiftDetailResponse findGiftDetail(Long giftId) {
        Gift gift = Optional.ofNullable(queryFactory
                        .selectFrom(QGift.gift)
                        .where(QGift.gift.giftId.eq(giftId)).fetchOne())
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.RESOURCE_NOT_FOUND));

        Product product = Optional.ofNullable(queryFactory
                        .selectFrom(QProduct.product)
                        .where(QProduct.product.productId.eq(gift.getReceipt().getProduct().getProductId()))
                        .fetchOne())
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.RESOURCE_NOT_FOUND));

        return GiftDetailResponse.of(gift, product);
    }
}

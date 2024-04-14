package org.kakaoshare.backend.domain.wish.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.wish.dto.WishDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.kakaoshare.backend.domain.member.entity.QMember.member;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.kakaoshare.backend.domain.wish.entity.QWish.wish;

@Repository
@RequiredArgsConstructor
public class WishRepositoryCustomImpl implements WishRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    
    @Override
    public List<WishDetail> findWishesByProviderId(final String providerId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                WishDetail.class,
                                product.productId,
                                product.name,
                                product.price,
                                product.photo,
                                wish.isPublic))
                .from(wish)
                .join(wish.member, member)
                .on(wish.member.providerId.eq(providerId))
                .join(wish.product, product)
                .fetch();
    }
}

package org.kakaoshare.backend.domain.wish.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.wish.dto.WishDetail;
import org.kakaoshare.backend.domain.wish.entity.QWish;
import org.kakaoshare.backend.domain.wish.entity.Wish;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

import static org.kakaoshare.backend.domain.member.entity.QMember.member;
import static org.kakaoshare.backend.domain.product.entity.QProduct.product;
import static org.kakaoshare.backend.domain.wish.entity.QWish.wish;

@Repository
@RequiredArgsConstructor
public class WishRepositoryCustomImpl implements WishRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    
    @Override
    public List<WishDetail> findWishDetailsByProviderId(final String providerId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                WishDetail.class,
                                wish.wishId,
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
    
    @Override
    public boolean isContainInWishList(Wish wish, Member member, Long productId) {
        BooleanExpression existsCondition = QWish.wish.member.eq(member)
                .and(QWish.wish.product.productId.eq(productId));
        
        Long count = queryFactory
                .select(QWish.wish.count())
                .from(QWish.wish)
                .where(existsCondition)
                .fetchOne();
        Assert.notNull(count,"count query must not to be null!");
        return count >0;
    }
}

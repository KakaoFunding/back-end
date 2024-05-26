package org.kakaoshare.backend.domain.wish.repository.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.kakaoshare.backend.domain.member.entity.Member;
import org.kakaoshare.backend.domain.wish.dto.FriendWishDetail;
import org.kakaoshare.backend.domain.wish.dto.MyWishDetail;
import org.kakaoshare.backend.domain.wish.dto.QFriendWishDetail;
import org.kakaoshare.backend.domain.wish.dto.QMyWishDetail;
import org.kakaoshare.backend.domain.wish.dto.QWishDetail;
import org.kakaoshare.backend.domain.wish.entity.QWish;
import org.kakaoshare.backend.domain.wish.entity.Wish;
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
    public List<MyWishDetail> findWishDetailsByProviderId(final String providerId) {
        return queryFactory
                .select(
                        new QMyWishDetail(wish.isPublic,
                                new QWishDetail(
                                        wish.wishId,
                                        product.productId,
                                        product.name,
                                        product.price,
                                        product.photo)))
                .from(wish)
                .join(wish.member, member)
                .on(wish.member.providerId.eq(providerId))
                .join(wish.product, product)
                .fetch();
    }
    
    @Override
    public List<FriendWishDetail> findWishDetailsByFriendProviderId(final String providerId,
                                                                    final String friendsProviderId) {
        QWish friendWish = new QWish("friendWish");
        QWish myWish = new QWish("myWish");
        
        return queryFactory
                .select(
                        new QFriendWishDetail(
                                new QWishDetail(
                                        friendWish.wishId,
                                        product.productId,
                                        product.name,
                                        product.price,
                                        product.photo),
                                JPAExpressions.select(myWish.count())
                                        .from(myWish)
                                        .where(myWish.member.providerId.eq(providerId)
                                                .and(myWish.product.eq(friendWish.product)))
                                        .gt(0L).as("isWished")
                        ))
                .from(friendWish)
                .join(friendWish.member, member)
                .on(member.providerId.eq(friendsProviderId)
                        .and(friendWish.isPublic.isTrue()))
                .join(friendWish.product, product)
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
        
        return count != null && count > 0;
    }
}